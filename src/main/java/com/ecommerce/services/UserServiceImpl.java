package com.ecommerce.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import com.ecommerce.constants.TemplateConstants;
import com.ecommerce.utils.AdminConfigUtils;
import com.ecommerce.helper.MustacheHelper;
import com.ecommerce.rabbitMQ.publisher.EmailPublisher;
import com.ecommerce.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.configurations.NullAwareBeans;
import com.ecommerce.decorators.EmailDetails;
import com.ecommerce.decorators.EmailDto;
import com.ecommerce.decorators.LoginDto;
import com.ecommerce.decorators.UserDto;
import com.ecommerce.decorators.UserPaginationApi;
import com.ecommerce.enums.Role;
import com.ecommerce.exceptions.AdminDeletionException;
import com.ecommerce.exceptions.PasswordNotMatchException;
import com.ecommerce.exceptions.UserAlreadyExistedException;
import com.ecommerce.exceptions.UserNotExistedException;
import com.ecommerce.models.AdminConfig;
import com.ecommerce.models.User;
import com.ecommerce.repositories.AdminConfigRepository;
import com.ecommerce.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidationUtils validationUtils;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final NullAwareBeans nullAwareBeans;
    private final AdminConfigRepository adminConfigRepository;
    private final AdminConfigUtils adminConfigUtils;
    private final MustacheHelper mustacheHelper;
    private final EmailPublisher emailPublisher;

    @Override
    public User creatUser(UserDto userDto) {
        Optional<User> user1 = userRepository.findByEmailAndSoftDeleteFalse(userDto.getEmail());

        if (user1.isPresent()) {
            throw new UserAlreadyExistedException();
        }

        // Validation
        validationUtils.nullFieldValidation(userDto.getEmail(), userDto.getPassword(), userDto.getPhoneNumber());
        validationUtils.emailValidate(userDto.getEmail());
        validationUtils.passwordValidation(userDto.getPassword());
        validationUtils.phoneNumberValidate(userDto.getPhoneNumber());

        User user = modelMapper.map(userDto, User.class);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFullName(user.getFirstName().concat(user.getLastName()));

        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();
        if (user.getRoles().contains(Role.ADMIN)) {
            if (adminConfig.getAdminEmails() == null) {
                adminConfig.setAdminEmails(List.of(userDto.getEmail()));
            } else {
                adminConfig.getAdminEmails().add(userDto.getEmail());
            }
            adminConfigRepository.save(adminConfig);
        }

        if (user.getRoles().contains(Role.SUPER_ADMIN)) {
            if (adminConfig.getSuperAdminEmails() == null) {
                adminConfig.setSuperAdminEmails(List.of(userDto.getEmail()));
            } else {
                adminConfig.getSuperAdminEmails().add(userDto.getEmail());
            }
            adminConfigRepository.save(adminConfig);
        } else {

            EmailDto emailDto = new EmailDto();
            emailDto.setUserName(user.getFullName());

            List<User> superAdminEmails = userRepository.filterByRole(List.of(Role.SUPER_ADMIN.toString()));

            if (!user.getRoles().contains(Role.ADMIN)) {

                sendMailToAllAdmins(superAdminEmails, emailDto);
                List<User> adminEmails = userRepository.filterByRole(List.of(Role.ADMIN.toString()));
                sendMailToAllAdmins(adminEmails, emailDto);

            } else {
                superAdminEmails.forEach((superAdmin) -> {
                    emailDto.setAdminName(user.getFullName());
                    emailDto.setSuperAdminName(superAdmin.getFullName());
                    notification(TemplateConstants.ADMIN, emailDto, superAdmin, "Admin created");
                });
            }
        }
        return userRepository.save(user);
    }

    private void sendMailToAllAdmins(List<User> adminEmails, EmailDto emailDto) {
        adminEmails.forEach((admin) -> {
            emailDto.setAdminName(admin.getFullName());
            notification(TemplateConstants.USER, emailDto, admin, "User created");
        });
    }

    private void notification(String templateConstant, EmailDto emailDto, User admin, String subject) {
        String content = mustacheHelper.setMessageContent(templateConstant, emailDto);
        emailPublisher.sendEmailNotification(new EmailDetails(admin.getEmail(), subject, content));
    }

    @Override
    public User getUser(String id) {
        return userRepository.findByIdAndSoftDeleteFalse(id).orElseThrow(UserNotExistedException::new);
    }

    @Override
    public void deleteUser(String id) {
        User user = getUser(id);

        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.SUPER_ADMIN)) {
            throw new AdminDeletionException();
        }
        user.setSoftDelete(true);
        userRepository.save(user);
    }

    @Override
    public User updateUser(String id, UserDto userDto) {
        if (userDto.getPhoneNumber() != null) {
            validationUtils.phoneNumberValidate(userDto.getPhoneNumber());
        }

        User user = getUser(id);
        userDto.setEmail(null);
        userDto.setPassword(null);

        try {
            nullAwareBeans.copyProperties(user, userDto);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        user.setFullName(user.getFirstName().concat(user.getLastName()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findBySoftDeleteFalse().orElseThrow(UserNotExistedException::new);
    }

    @Override
    public User login(LoginDto loginDTO) {
        User user = userRepository.findByEmailAndSoftDeleteFalse(loginDTO.getEmail())
                .orElseThrow(UserNotExistedException::new);
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }
        return user;
    }

    @Override
    public Page<User> filterSearchAndPagination(UserPaginationApi userPaginationApi) {
        return userRepository.filterSearchAndPagination(userPaginationApi);
    }
}
