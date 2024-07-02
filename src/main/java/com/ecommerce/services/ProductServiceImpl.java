package com.ecommerce.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.ecommerce.constants.TemplateConstants;
import com.ecommerce.utils.AdminConfigUtils;
import com.ecommerce.helper.MustacheHelper;
import com.ecommerce.rabbitMQ.publisher.EmailPublisher;
import com.ecommerce.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ecommerce.configurations.NullAwareBeans;
import com.ecommerce.decorators.EmailDetails;
import com.ecommerce.decorators.EmailDto;
import com.ecommerce.decorators.ProductCancellationDto;
import com.ecommerce.decorators.ProductDto;
import com.ecommerce.decorators.ProductCountDto;
import com.ecommerce.decorators.ProductPaginationApi;
import com.ecommerce.decorators.RequestSession;
import com.ecommerce.decorators.UserDetailsDto;
import com.ecommerce.enums.Role;
import com.ecommerce.exceptions.ProductNotExistedException;
import com.ecommerce.exceptions.ProductPurchaseException;
import com.ecommerce.exceptions.ProductExistedException;
import com.ecommerce.exceptions.ProductNotAvailableException;
import com.ecommerce.models.AdminConfig;
import com.ecommerce.models.Product;
import com.ecommerce.models.User;
import com.ecommerce.repositories.AdminConfigRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final NullAwareBeans nullAwareBeans;
    private final RequestSession requestSession;
    private final MustacheHelper mustacheHelper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailPublisher emailPublisher;
    private final AdminConfigUtils adminConfigUtils;
    private final AdminConfigRepository adminConfigRepository;
    private final ValidationUtils validationUtils;

    // Get Product from product id
    public Product getProduct(String productId) {
        return productRepository.findByIdAndSoftDeleteFalse(productId)
                .orElseThrow(() -> new ProductNotExistedException("Product with given id is not existed"));
    }

    // Get product from product name
    public Product getProductByName(String productName, String color) {
        return productRepository.findByNameAndColorAndSoftDeleteFalse(productName, color)
                .orElseThrow(() -> new ProductNotExistedException("Product with given name or color is not existed"));
    }

    // Send notification to admin when super admin add, delete or update any product
    public void sendNotification(Product product, String status) {
        User superAdmin = userService.getUser(requestSession.getJwtUser().getUserId());

        EmailDto emailDto = new EmailDto();
        emailDto.setProductName(product.getName());
        emailDto.setStatus(status);
        emailDto.setSuperAdminName(superAdmin.getFullName());

        List<User> adminEmails = userRepository.filterByRole(List.of(Role.ADMIN.toString()));

        adminEmails.forEach((admin) -> {
            emailDto.setAdminName(admin.getFullName());
            notification(TemplateConstants.PRODUCT, emailDto, admin.getEmail(), "Product " + status);
        });
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findByNameAndColorAndSoftDeleteFalse(productDto.getName(),
                productDto.getColor());

        if (productOptional.isPresent()) {
            throw new ProductExistedException("Product with given name and color is already existed");
        }

        validationUtils.productPriceValidation(productDto.getPrice());
        validationUtils.productDateValidation(productDto.getEstablishedDate(), productDto.getEndDate());
        validationUtils.productDiscountRateValidation(productDto.getDiscountRate());

        Product product = modelMapper.map(productDto, Product.class);
        sendNotification(product, "added");
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productId, boolean deleteFromUserProfile) {
        Product product = getProduct(productId);

        product.setSoftDelete(true);
        productRepository.save(product);

        if (deleteFromUserProfile) {
            List<User> allUsers = userService.getAllUsers();

            allUsers.forEach(user -> {
                if (user.getProductList() != null) {
                    if (user.getProductList().contains(productId)) {
                        user.getProductList().remove(productId);
                        userRepository.save(user);

                        EmailDto emailDto = new EmailDto();
                        emailDto.setUserName(user.getFullName());
                        emailDto.setProductName(product.getName());

                        // send mail to user for that product he purchased is deleted
                        notification(TemplateConstants.DELETE, emailDto, user.getEmail(), "Product deleted");
                    }
                }
            });
        }
        sendNotification(product, "deleted");
    }

    private void notification(String templateConstant, EmailDto emailDto, String user, String subject) {
        String content = mustacheHelper.setMessageContent(templateConstant, emailDto);
        emailPublisher.sendEmailNotification(new EmailDetails(user, subject, content));
    }

    @Override
    public Product updateProduct(String productId, ProductDto productDto) {
        Product product = getProduct(productId);

        if (productDto.getPrice() != null) {
            validationUtils.productPriceValidation(productDto.getPrice());
        }
        if (productDto.getEstablishedDate() != null && productDto.getEndDate() != null) {
            validationUtils.productDateValidation(productDto.getEstablishedDate(), productDto.getEndDate());
        }
        if (productDto.getDiscountRate() != null) {
            validationUtils.productDiscountRateValidation(productDto.getDiscountRate());
        }

        try {
            nullAwareBeans.copyProperties(product, productDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        sendNotification(product, "updated");

        return productRepository.save(product);
    }

    @Override
    public Page<Product> filterSearchAndPagination(ProductPaginationApi productPaginationApi) {
        return productRepository.filterSearchAndPagination(productPaginationApi);
    }

    // check for special user
    public void specialUser(AdminConfig adminConfig, LocalDate date, User user, String userId) {

        Map<String, List<String>> map = adminConfig.getDailySellProduct().get(date);
        int size = map.get(userId).size();

        // set special user if it purchases more products than other
        for (List<String> list : map.values()) {
            if (size < list.size()) {
                user.setSpecialUser(false);
                break;
            }
            user.setSpecialUser(true);
        }
        userRepository.save(user);

        // set special user false to others if this user is special user
        if (user.isSpecialUser()) {
            List<User> allUsers = userService.getAllUsers();
            allUsers.forEach(user2 -> {
                user2.setSpecialUser(false);
                userRepository.save(user2);
            });
        }
    }

    @Override
    public Product buyProduct(String productName, String color) {

        // get product
        Product product = getProductByName(productName, color);

        // check validation
        if ((new Date()).before(product.getEstablishedDate())) {
            throw new ProductNotAvailableException("Product will launch in future");
        } else if (new Date().after(product.getEndDate())) {
            throw new ProductNotAvailableException("Product is expired");
        } else if (!product.getStock()) {
            throw new ProductNotAvailableException("Product is out of stock");
        }

        String userId = requestSession.getJwtUser().getUserId();
        User user = userService.getUser(userId);

        // check if product already purchased
        if (user.getProductList() != null) {
            if (user.getProductList().contains(product.getId())) {
                throw new ProductPurchaseException("This product is already once purchased");
            }
        }

        // check that user cancel that product in last 30 minutes or not
        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();
        checkIfProductCancelledRecently(adminConfig, product, userId);

        // increase sell count of product
        product.setProductSellCount(product.getProductSellCount() + 1);
        productRepository.save(product);

        LocalDate date = getLocalDate();

        // add product into daily sell product list
        addProductIntoDailySellProductList(adminConfig, date, userId, product);

        // Check for special user
        if (!user.isSpecialUser()) {
            specialUser(adminConfig, date, user, userId);
        }

        adminConfigRepository.save(adminConfig);

        // set top-selling product if it reaches the top-selling number
        if (product.getProductSellCount() >= adminConfig.getTopSellingNumber()) {
            product.setTopSelling(true);
            productRepository.save(product);
        }

        // set discount and make details for invoice mail
        double discount = (product.getDiscountRate() * product.getPrice()) / 100;
        double finalPrice = product.getPrice() - discount;

        EmailDto emailDto = new EmailDto();
        emailDto.setUserName(user.getFullName());
        emailDto.setProductName(product.getName());
        emailDto.setPrice(product.getPrice());
        emailDto.setDiscount(discount);
        emailDto.setFinalPrice(finalPrice);

        // send mail to user for purchase
        String content = mustacheHelper.setMessageContent(TemplateConstants.PURCHASE, emailDto);
        notification(TemplateConstants.PURCHASE, emailDto, user.getEmail(), "Product purchase");

        // set mail to all admins and super admins when user purchase a product
        productPurchaseNotification(adminConfig.getAdminEmails(), content);
        productPurchaseNotification(adminConfig.getSuperAdminEmails(), content);

        // add product into user's product list
        if (user.getProductList() == null) {
            user.setProductList(List.of(product.getId().toString()));
        } else {
            user.getProductList().add(product.getId().toString());
        }
        userRepository.save(user);
        return product;
    }

    private void checkIfProductCancelledRecently(AdminConfig adminConfig, Product product, String userId) {
        List<ProductCancellationDto> cancelledProducts = adminConfig.getCancelledProducts();
        if (cancelledProducts != null) {
            cancelledProducts.forEach((cancellation) -> {
                if (cancellation.getProductId().equals(product.getId()) && cancellation.getCancelledBy().equals(userId)
                        && cancellation.getCancelledTime().isAfter(LocalDateTime.now().minusMinutes(30))) {
                    throw new ProductPurchaseException(
                            "Product is currently not available because it is cancelled recently");
                }
            });
        }
    }

    private void productPurchaseNotification(List<String> adminEmails, String content) {
        adminEmails.forEach(admin -> emailPublisher.sendEmailNotification(new EmailDetails(admin, "Product purchase", content)));
    }

    private void addProductIntoDailySellProductList(AdminConfig adminConfig, LocalDate date, String userId, Product product) {
        if (adminConfig.getDailySellProduct() == null) {
            adminConfig.setDailySellProduct(Map.of(date, Map.of(userId, List.of(product.getId()))));
        } else if (adminConfig.getDailySellProduct().containsKey(date)) {

            if (adminConfig.getDailySellProduct().get(date).containsKey(userId)) {
                List<String> list = adminConfig.getDailySellProduct().get(date).get(userId);
                list.add(product.getId());
                adminConfig.getDailySellProduct().get(date).put(userId, list);
            } else {
                adminConfig.getDailySellProduct().get(date).put(userId, List.of(product.getId()));
            }

        } else {
            adminConfig.getDailySellProduct().put(date, Map.of(userId, List.of(product.getId())));
        }
    }

    @Override
    public void cancelProduct(String productId) {
        String userId = requestSession.getJwtUser().getUserId();
        User user = userService.getUser(userId);

        if (!user.getProductList().contains(productId)) {
            throw new ProductNotExistedException("Product with given id is not purchased");
        }

        // Remove product from user profile
        user.getProductList().remove(productId);
        userRepository.save(user);

        // Remove product count from product profile
        Product product = getProduct(productId);
        product.setProductSellCount(product.getProductSellCount() - 1);
        productRepository.save(product);

        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();
        // Set topSelling false if it is not greater than top selling number
        setTopSelling(product, adminConfig);
        // Remove product from daily sell products
        LocalDate date = getLocalDate();
        removeDailySellProducts(productId, adminConfig, date, userId);
        // Set daily cancel products
        setDailyCancelProducts(productId, adminConfig, date);
        // check for special user
        if (user.isSpecialUser()) {
            specialUser(adminConfig, date, user, userId);
        }

        // restrict user from buying same product for next 30 minutes
        restrictionFromBuying(productId, adminConfig, userId);

        EmailDto emailDto = new EmailDto();
        emailDto.setUserName(user.getFullName());
        emailDto.setProductName(product.getName());
        // set mail to all admins and super admins when user purchase a product
        sendProductCancelMail(adminConfig.getAdminEmails(), emailDto);
        sendProductCancelMail(adminConfig.getSuperAdminEmails(), emailDto);

        adminConfigRepository.save(adminConfig);
    }

    private void restrictionFromBuying(String productId, AdminConfig adminConfig, String userId) {
        if (adminConfig.getCancelledProducts() == null) {
            adminConfig
                    .setCancelledProducts(List.of(new ProductCancellationDto(productId, userId, LocalDateTime.now())));
        } else {
            adminConfig.getCancelledProducts().add(new ProductCancellationDto(productId, userId, LocalDateTime.now()));
        }
    }

    private void sendProductCancelMail(List<String> adminEmails, EmailDto emailDto) {
        adminEmails.forEach(admin -> {
            emailDto.setAdminName(admin);
            notification(TemplateConstants.CANCEL, emailDto, admin, "Product cancelled");
        });
    }

    private void setDailyCancelProducts(String productId, AdminConfig adminConfig, LocalDate date) {
        if (adminConfig.getDailyCancelProduct() == null) {
            adminConfig.setDailyCancelProduct(Map.of(date, List.of(productId)));
        } else if (adminConfig.getDailyCancelProduct().containsKey(date)) {
            List<String> list2 = adminConfig.getDailyCancelProduct().get(date);
            list2.add(productId);
            adminConfig.getDailyCancelProduct().put(date, list2);
        } else {
            adminConfig.getDailyCancelProduct().put(date, List.of(productId));
        }
    }

    private void removeDailySellProducts(String productId, AdminConfig adminConfig, LocalDate date, String userId) {
        Map<LocalDate, Map<String, List<String>>> dailySellProduct = adminConfig.getDailySellProduct();

        for (LocalDate date2 = date; date2.isBefore(date) || date2.equals(date); date2 = date2.minusDays(1)) {
            Map<String, List<String>> productMap = dailySellProduct.get(date2);
            if (productMap != null) {
                List<String> productList = productMap.get(userId);
                if (productList != null) {
                    if (productList.contains(productId)) {
                        productList.remove(productId);
                        break;
                    }
                }
            }
        }
    }

    private void setTopSelling(Product product, AdminConfig adminConfig) {
        if (product.getProductSellCount() < adminConfig.getTopSellingNumber()) {
            product.setTopSelling(false);
            productRepository.save(product);
        }
    }

    @Override
    public ProductCountDto productDetails() {

        ProductCountDto productCountDto = new ProductCountDto();
        productCountDto.setAvailableProductCount(productRepository.getAvailableProduct(new Date()));
        productCountDto.setExpiredProductCount(productRepository.getExpiredProduct(new Date()));
        productCountDto.setCominSoonProductCount(productRepository.getComingSoonProduct(new Date()));

        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();
        LocalDate date = getLocalDate();

        // set daily sell product count
        Map<LocalDate, Map<String, List<String>>> dailySellProduct = adminConfig.getDailySellProduct();
        if (dailySellProduct != null && dailySellProduct.containsKey(date)) {
            Map<String, List<String>> map = dailySellProduct.get(date);
            int count = 0;

            for (List<String> list : map.values()) {
                count += list.size();
            }
            productCountDto.setDailySellProduct(count);
        }

        // set daily cancel product count
        Map<LocalDate, List<String>> dailyCancelProduct = adminConfig.getDailyCancelProduct();
        if (dailyCancelProduct != null && dailyCancelProduct.containsKey(date)) {
            productCountDto.setDailyCancelProduct(dailyCancelProduct.get(date).size());
        }
        return productCountDto;
    }

    private LocalDate getLocalDate() {
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public List<Product> purchaseProductDetails(User user) {
        return productRepository.findAllByIdAndSoftDeleteFalse(user.getProductList()).reversed();
    }

    @Override
    public List<UserDetailsDto> userDetails() {
        User user = userService.getUser(requestSession.getJwtUser().getUserId());
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.SUPER_ADMIN)) {
            return getUserDetailsDtoList();
        }
        return List.of(getUserDetailsDto(user));
    }

    @NotNull
    private UserDetailsDto getUserDetailsDto(User user) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        modelMapper.map(user, userDetailsDto);
        if (user.getProductList() != null) {
            userDetailsDto.setProductList(purchaseProductDetails(user));
        }
        return userDetailsDto;
    }

    @NotNull
    private List<UserDetailsDto> getUserDetailsDtoList() {
        List<UserDetailsDto> userDetailsDtoList = new ArrayList<>();
        userService.getAllUsers().forEach(user -> userDetailsDtoList.add(getUserDetailsDto(user)));
        return userDetailsDtoList;
    }

}
