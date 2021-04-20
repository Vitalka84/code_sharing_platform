package platform.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import platform.models.SharedCode;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedCodeDAO extends CrudRepository<SharedCode, Integer>, PagingAndSortingRepository<SharedCode, Integer>, JpaRepository<SharedCode, Integer> {
    SharedCode findSharedCodeByRecordId(int recordId);

    SharedCode findSharedCodeByRecordUUID(UUID recordUUID);

    List<SharedCode> findTop10ByOrderByRecordIdDesc();
}
