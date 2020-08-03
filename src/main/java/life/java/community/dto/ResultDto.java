package life.java.community.dto;

import lombok.Data;

@Data
public class ResultDto {
    private Integer code;
    private String message;

    public static ResultDto errorOf(Integer code, String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }
    public static ResultDto ok(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
}
