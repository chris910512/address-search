import java.util.Objects;

public class AddressDto {
    private final String level1; // 도/시
    private final String level2; // 시/구/군
    private final String level3; // 동/면
    private final String level4; // 리
    private final String level5; // 로/길

    public AddressDto(String level1, String level2, String level3, String level4, String level5) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.level4 = level4;
        this.level5 = level5;
    }

    @Override
    public String toString() {
        if(level4 == null || "".equals(level4)) {
            return this.level1 + " " + this.level2 + " " + this.level3 + " " + this.level5;
        } else {
            return this.level1 + " " + this.level2 + " " + this.level3 + " " + this.level4 + " " + this.level5;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDto that)) return false;
        return Objects.equals(this.level1, that.level1) && Objects.equals(this.level2, that.level2) && Objects.equals(this.level3, that.level3) && Objects.equals(this.level4, that.level4) && Objects.equals(this.level5, that.level5);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level1, level2, level3, level4, level5);
    }
}
