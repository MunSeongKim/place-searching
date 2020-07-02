package com.mskim.place_searching.place;

import com.mskim.place_searching.place.dto.PlaceDto;
import com.mskim.place_searching.place.service.PlaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PlaceDtoServiceTest {
    private final Logger logger = LoggerFactory.getLogger(PlaceDtoServiceTest.class);

    @Autowired
    private PlaceService placeService;


    @Test
    void PlaceService_장소_조회() {
        String keyword = "서울역";
        int size = 10;
        int page = 1;
        PlaceDto dto = placeService.retrievePlace(keyword, page, size);

        logger.info(() -> "Places: " + dto);

        then(dto.getPlaces()).isNotNull();
        then(dto.getPlaces().size()).isEqualTo(size);

        then(dto.getPager()).isNotNull();
        then(dto.getPager().getTotalItemCount()).isEqualTo(35);
        then(dto.getPager().getTotalPageCount()).isEqualTo(4);
    }
}