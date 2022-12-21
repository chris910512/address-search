import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;

public class AddressService {

    // 도로명주소 출처: https://business.juso.go.kr/addrlink/attrbDBDwld/attrbDBDwldList.do?cPath=99MD&menu=%EC%A3%BC%EC%86%8CDB
    // 파일이 EUC-KR로 인코딩 되어 있어서 전부 UTF-8로 변환하는 작업을 먼저 진행함.

    /**
     * 도로명은 붙여쓰고 도로명과 건물번호 사이는 띄어 쓴다. ex) 국회대로62길 9
     * 건물번호와 동/층/호 사이에는 쉼표를 사용한다. ex) 석주로 89, 201동
     * */

    static HashSet<AddressDto> addressHashSet = new HashSet<>();

    void init() throws IOException {
        final File folder = new File(".\\resources\\202211_address");
        final File[] fileList = Objects.requireNonNull(folder.listFiles());

        for(File item : fileList) {
            FileReader fr = new FileReader(item);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
//                System.out.println(line);
                // 2: 도/시 3: 시/구/군 4: 동/면 5:리 10:로/길
                String[] split = line.split("\\|");
                addressHashSet.add(new AddressDto(split[2], split[3], split[4], split[5], split[10]));
            }
        }
    }

    // 1. 도로명 주소를 제대로 입력한 case
    // 길이 있으면 로까지 함께 찾는다.
    // 길이 없으면 로만 찾는다.

    // 2. 제대로 입력하지 않은 case
    // 시/군/구를 입력하지 않은 case
    // 로/길 앞뒤에 공백이 있는 case
    // 쉼표 외 특수문자가 있는 case
    // 오타가 있는 case


}
