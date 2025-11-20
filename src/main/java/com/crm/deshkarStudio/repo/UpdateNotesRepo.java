package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.UpdateNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateNotesRepo extends JpaRepository<UpdateNotes, Long> {

    List<UpdateNotes> findByPurchaseId(long id);

}
