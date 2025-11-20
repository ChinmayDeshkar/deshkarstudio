package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.UpdateNotes;

import java.util.List;

public interface NoteService {

    UpdateNotes addUpdateNote(CustomerPurchases purchases, String note);

    List<UpdateNotes> getNotes(long id);
}
