package com.kulakwetu.identity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rbac")
public class RbacDemoController {

    @GetMapping("/order-assignment")
    @PreAuthorize("hasAuthority('ORDER_ASSIGN')")
    public String orderAssignProtected() {
        return "ORDER_ASSIGN permission granted";
    }

    @GetMapping("/product-approval")
    @PreAuthorize("hasAuthority('AGRISOL_PRODUCT_APPROVE')")
    public String productApprovalProtected() {
        return "AGRISOL_PRODUCT_APPROVE permission granted";
    }
}
