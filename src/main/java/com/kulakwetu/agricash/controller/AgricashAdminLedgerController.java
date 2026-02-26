package com.kulakwetu.agricash.controller;

import com.kulakwetu.agricash.dto.LedgerJournalResponse;
import com.kulakwetu.agricash.dto.PostJournalRequest;
import com.kulakwetu.agricash.service.LedgerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agricash/admin/ledger")
@RequiredArgsConstructor
@Tag(name = "AGRICASH - ADMIN LEDGER")
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
public class AgricashAdminLedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/journals")
    public List<LedgerJournalResponse> journals() { return ledgerService.journals(); }

    @PostMapping("/journals")
    public LedgerJournalResponse postJournal(@Valid @RequestBody PostJournalRequest request) {
        return ledgerService.postJournal(request);
    }

    @PostMapping("/journals/{id}/reverse")
    public LedgerJournalResponse reverse(@PathVariable UUID id) {
        return ledgerService.reverseJournalProRata(id);
    }
}
