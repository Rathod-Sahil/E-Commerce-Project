package com.ecommerce.controllers;

import java.util.List;

import com.ecommerce.annotations.Access;
import com.ecommerce.constants.ResponseConstants;
import com.ecommerce.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.decorators.DataResponse;
import com.ecommerce.decorators.PageResponse;
import com.ecommerce.decorators.ProductDto;
import com.ecommerce.decorators.ProductCountDto;
import com.ecommerce.decorators.ProductPaginationApi;
import com.ecommerce.helper.Response;
import com.ecommerce.decorators.UserDetailsDto;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product-Api")
@RequiredArgsConstructor
@RequestMapping("/product")
@RestController
public class ProductController {

    private final ProductService productService;

    @Access(role = Role.SUPER_ADMIN)
    @Operation(summary = "Add product in stock" , description = "Add new product into stock for sell by super admin")
    @PostMapping(value = "/add" , name = "addProduct")
    public DataResponse<Product> addProduct(@RequestBody ProductDto productDto){
        return new DataResponse<>(productService.addProduct(productDto),Response.getOkResponse(ResponseConstants.ADD_NEW_PRODUCT));
    }

    @Access(role = Role.SUPER_ADMIN)
    @Operation(summary = "Delete product from stock" , description = "Delete an existing product from stock by super admin")
    @DeleteMapping(value = "/{productId}" ,name =  "deleteProduct")
    public DataResponse<Object> deleteProduct(@PathVariable String productId,@RequestParam boolean deleteFromUserProfile){
        productService.deleteProduct(productId, deleteFromUserProfile);
        return new DataResponse<>(null,Response.getOkResponse(ResponseConstants.DELETE_PRODUCT));
    }

    @Access(role = Role.SUPER_ADMIN)
    @Operation(summary = "Update product details in stock" , description = "Update an existing product details in stock by super admin")
    @PutMapping(value = "/{productId}" , name = "updateProduct")
    public DataResponse<Product> updateProduct(@PathVariable String productId,@RequestBody ProductDto productDto){
        return new DataResponse<>(productService.updateProduct(productId, productDto),Response.getOkResponse(ResponseConstants.UPDATE_PRODUCT));
    }

    @Access(role = {Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @Operation(summary = "Find products from stock" , description = "Find products from stock based on specific criteria")
    @PostMapping(value = "/find" , name = "findProducts")
    public PageResponse<Product> getProducts(@RequestBody ProductPaginationApi productPaginationApi){
        return new PageResponse<>(productService.filterSearchAndPagination(productPaginationApi),Response.getOkResponse(ResponseConstants.GET_PRODUCTS));
    }

    @Access(role = Role.USER)
    @Operation(summary = "Buy a new product from stock" , description = "Buy a new product of specific color from stock")
    @GetMapping(value = "/buy" , name = "buyProduct")
    public DataResponse<Product> buyProduct(@RequestParam String productName, @RequestParam String color){
        return new DataResponse<>(productService.buyProduct(productName, color),Response.getOkResponse(ResponseConstants.BUY_PRODUCT));
    }

    @Access(role = Role.SUPER_ADMIN)
    @Operation(summary = "Get product count details" , description = "Get information related to product count by super admin")
    @GetMapping(value = "/productCount" , name = "productCount")
    public DataResponse<ProductCountDto> productCount(){
        return new DataResponse<>(productService.productDetails(), Response.getOkResponse(ResponseConstants.PRODUCT_COUNT));
    }

    @Access(role = Role.USER)
    @Operation(summary = "Cancel product from cart" , description = "Cancel product from cart")
    @GetMapping(value = "/cancel/{productId}", name = "CancelProduct")
    public DataResponse<Object> cancelProduct(String productId){
        productService.cancelProduct(productId);
        return new DataResponse<>(null,Response.getOkResponse(ResponseConstants.CANCEL_PRODUCT));
    }

    @Access(role = {Role.USER ,Role.ADMIN ,Role.SUPER_ADMIN})
    @Operation(summary = "Get user details" , description = "Get user details with product details")
    @GetMapping(value = "/userDetails", name = "UserDetails")
    public DataResponse<List<UserDetailsDto>> userDetails(){
        return new DataResponse<>(productService.userDetails(),Response.getOkResponse(ResponseConstants.USER_DETAILS));
    }
}
