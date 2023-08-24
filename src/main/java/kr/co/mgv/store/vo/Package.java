package kr.co.mgv.store.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("Package")
public class Package {

    private int no;
    private String name;
    private int price;
    private String image;
    private String composition;
    private Category category;

    public Package(int no) {
        this.no = no;
    }
}
