package fourman.backend.domain.eventBoard.service;

import fourman.backend.domain.eventBoard.controller.requestForm.EventRequestForm;
import fourman.backend.domain.eventBoard.service.response.EventDetailResponse;
import fourman.backend.domain.eventBoard.service.response.EventListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    Long registerEvent(List<MultipartFile> thumbnail, List<MultipartFile> fileList, EventRequestForm eventRequestForm);
    List<EventListResponse> list();
    EventDetailResponse read(Long eventId);

}
