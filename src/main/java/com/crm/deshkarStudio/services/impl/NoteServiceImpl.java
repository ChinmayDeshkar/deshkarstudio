package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.UpdateNotes;
import com.crm.deshkarStudio.repo.UpdateNotesRepo;
import com.crm.deshkarStudio.services.NoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UpdateNotesRepo updateNotesRepo;

    @Override
    public UpdateNotes addUpdateNote(CustomerPurchases purchases, String note) {
        UpdateNotes notes = new UpdateNotes();
        notes.setPurchaseId(purchases.getPurchaseId());
        notes.setUpdatedBy(purchases.getUpdatedBy());
        notes.setNote(note);

        updateNotesRepo.save(notes);
        return null;
    }

    @Override
    public List<UpdateNotes> getNotes(long id) {

        return updateNotesRepo.findByPurchaseId(id);
    }
}
