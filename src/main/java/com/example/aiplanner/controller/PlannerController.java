package com.example.aiplanner.controller;

import com.example.aiplanner.model.planner.PlannerEntity;
import com.example.aiplanner.model.planner.PlannerRequestDto;
import com.example.aiplanner.model.planner.PlannerResponseDto;
import com.example.aiplanner.service.AIPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlannerController {

    private final AIPlannerService aiPlannerService;

    @PostMapping("/planner-create")
    public PlannerEntity createPlanner(@RequestParam Long userId,
                                       @RequestBody List<String> tasks) {
        // AI 플래너 생성 서비스 호출
        return aiPlannerService.generateAIPlanner(userId, tasks);
    }

    @PostMapping("/planner-generate")
    public ResponseEntity<PlannerResponseDto> generatePlanner(@RequestParam Long userId,
                                                              @RequestBody PlannerRequestDto requestDto) {
        PlannerEntity plannerEntity = aiPlannerService.generateAIPlanner(userId, requestDto.getTasks());
        PlannerResponseDto responseDto = new PlannerResponseDto(plannerEntity);
        return ResponseEntity.ok(responseDto);
    }

}
