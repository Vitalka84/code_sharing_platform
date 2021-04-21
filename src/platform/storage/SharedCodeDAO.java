package platform.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import platform.models.SharedCode;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedCodeDAO extends CrudRepository<SharedCode, Integer>, PagingAndSortingRepository<SharedCode, Integer>, JpaRepository<SharedCode, Integer> {
    SharedCode findSharedCodeByRecordId(int recordId);

    @Query("select sc from SharedCode sc where (sc.allowedViews=0 or sc.allowedViews>sc.views) " +
            "and (sc.viewingTime=0 or (sc.dateUnixTime+sc.viewingTime)>=:currentTimeStamp)" +
            "and sc.recordUUID =:recordUUID")
    SharedCode findSharedCodeByRecordUUID(@Param("recordUUID") UUID recordUUID, @Param("currentTimeStamp") long currentTimeStamp);

    @Query(nativeQuery = true, value = "select sc.* from shared_codes sc where sc.VIEWING_TIME=0 and sc.ALLOWED_VIEWS=0 order by sc.RECORD_ID desc limit 10")
    List<SharedCode> findTop10();
}
