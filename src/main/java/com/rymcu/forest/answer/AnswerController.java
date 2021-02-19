package com.rymcu.forest.answer;

import com.rymcu.forest.dto.AnswerDTO;
import com.rymcu.forest.dto.result.Result;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.util.HttpUtils;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.v1.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/** @author ronger */
@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {

  private static final String ANSWER_API_URL = "http://101.132.237.86:8089/question";

  @GetMapping("/today")
  public Result<String> today() throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    String result = HttpUtils.sendGet(ANSWER_API_URL + "/record/" + user.getIdUser());
    return Result.OK(result);
  }

  @PostMapping("/answer")
  public Result<String> answer(@RequestBody AnswerDTO answerDTO) throws BaseApiException {
    User user = UserUtils.getCurrentUserByToken();
    Map params = new HashMap<>(3);
    params.put("userId", user.getIdUser());
    params.put("answer", answerDTO.getAnswer());
    params.put("subjectQuestionId", answerDTO.getIdSubjectQuestion());
    String result = HttpUtils.sendPost(ANSWER_API_URL + "/answer/everyday", params);
    return Result.OK(result);
  }

  @GetMapping("/get-answer")
  public Result<String> getAnswer(Integer idSubjectQuestion) {
    String result = HttpUtils.sendGet(ANSWER_API_URL + "/show-answer/" + idSubjectQuestion);
    return Result.OK(result);
  }
}
