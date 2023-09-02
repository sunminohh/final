package kr.co.mgv.theater.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SeatsDto {
    private int screenId;
    private List<String> seatNos;
}
