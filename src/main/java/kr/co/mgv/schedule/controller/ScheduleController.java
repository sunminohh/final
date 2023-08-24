package kr.co.mgv.schedule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.schedule.dto.CheckScheduleDto;
import kr.co.mgv.schedule.dto.DateWithMovieDto;
import kr.co.mgv.schedule.dto.ScheduleRegistFormDto;
import kr.co.mgv.schedule.service.ScheduleService;
import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.theater.dto.TheaterAndDateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	private final ScheduleService scheduleService;

	@GetMapping("/list")
	@ResponseBody
	public DateWithMovieDto scheduleList(int theaterNo, String date, String time) {
		TheaterAndDateDto dto = new TheaterAndDateDto(theaterNo, date, time);
		return scheduleService.getScheduleList(dto);
	}

	@GetMapping("/checkSchedule")
	@ResponseBody
	public CheckScheduleDto checkSchedule(int theaterNo) {
		return scheduleService.checkSchedule(theaterNo);
	}

	@GetMapping("/admin/list")
	@ResponseBody
	public List<Map<String, Object>> scheduleList(String date, int screen) {
		return scheduleService.getScheduleList(date, screen);
	}

	
	@PostMapping("admin/regist")
	@ResponseBody
	public String registSchedule(@RequestBody ScheduleRegistFormDto dto) {
		log.info("dto->{}",dto);
		return scheduleService.registSchedule(dto);
	}
	
	
}
