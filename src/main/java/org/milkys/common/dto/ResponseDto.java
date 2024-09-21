package org.milkys.common.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 *다양한 유형의 응답을 표현하는 데 사용되는 일반화된 응답 객체
 * 응답 데이터, 상태 코드, 오류 정보를 포함할 수 있으며, 여러 생성자와 유틸리티 메서드를 제공
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> implements Serializable {
    /**
     * 직렬화 가능하다는 것을 명시합니다. 직렬화는 객체를 바이트 스트림으로 변환하여 저장하거나 전송할 때 사용
     */
    private static final long serialVersionUID = 1289378297382227732L;
    /**
     * 제네릭 타입 T의 응답 데이터를 저장 T는 호출 시 구체적인 타입으로 대체
     */
    private T ResultData;
    private Long ResultCount;
    private int status;
    private ErrorDto error;

    /**
     * 다양한 생성자들
     */
    public ResponseDto(T resultData, int status){
        this.ResultData = resultData;
        this.status = status;
    }
    public ResponseDto(T resultData){
        this.ResultData = resultData;
    }
    public ResponseDto(T resultData, Long resultCount){
        this.ResultData = resultData;
        this.ResultCount = resultCount;
    }
    public ResponseDto(ErrorDto errorDto){
        this.ResultData = null;
        this.error = errorDto;
    }


    /**
     * 정적 팩토리 메서드
     * ResultData를 초기화하는 ResponseDto를 반환하는 정적 메서드
     */
    public static <T> ResponseDto<T> from(T resultData){
        return new ResponseDto<T>(resultData);
    }

    /**
     *  오류 메시지와 상태를 사용하여 ErrorDto를 생성하고,
     *  이를 담은 ResponseDto를 반환합니다. ErrorDto.of 메서드를 사용하여 오류 정보를 생성
     */
    public static ResponseDto<?> ERROR(String errorMessage, HttpStatus status){
        return new ResponseDto(ErrorDto.of(errorMessage, status));
    }

    /**
     * 예외(Throwable)와 상태를 사용하여 ErrorDto를 생성하고,
     * 이를 담은 ResponseDto를 반환합니다. 예외로부터 오류 정보를 생성하는 방법을 제공
     */
    public static ResponseDto<?> ERROR(Throwable throwable, HttpStatus status){
        return new ResponseDto(ErrorDto.of(throwable, status));
    }
}
