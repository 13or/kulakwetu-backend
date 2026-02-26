package com.kulakwetu.admin.controller;

import com.kulakwetu.admin.dto.SlideRequest;
import com.kulakwetu.admin.dto.SlideResponse;
import com.kulakwetu.admin.enums.SlideTarget;
import com.kulakwetu.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/slides")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
@Tag(name = "ADMIN - SLIDES")
public class AdminSlidesController {

    private final AdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SlideResponse createSlide(@Valid @RequestBody SlideRequest request) {
        return adminService.createSlide(request);
    }

    @PutMapping("/{id}")
    public SlideResponse updateSlide(@PathVariable UUID id, @Valid @RequestBody SlideRequest request) {
        return adminService.updateSlide(id, request);
    }

    @GetMapping("/{id}")
    public SlideResponse getSlide(@PathVariable UUID id) {
        return adminService.getSlide(id);
    }

    @GetMapping
    public Page<SlideResponse> listSlides(@RequestParam(required = false) SlideTarget target, Pageable pageable) {
        return adminService.listSlides(target, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSlide(@PathVariable UUID id) {
        adminService.deleteSlide(id);
    }
}
