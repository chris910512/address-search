import java.io.*;
import java.util.*;

public class AddressService {

    // 도로명주소 출처: https://business.juso.go.kr/addrlink/attrbDBDwld/attrbDBDwldList.do?cPath=99MD&menu=%EC%A3%BC%EC%86%8CDB
    // 파일이 EUC-KR로 인코딩 되어 있어서 전부 UTF-8로 변환하는 작업을 먼저 진행함.

    /**
     * 도로명은 붙여쓰고 도로명과 건물번호 사이는 띄어 쓴다. ex) 국회대로62길 9
     * 건물번호와 동/층/호 사이에는 쉼표를 사용한다. ex) 석주로 89, 201동
     * */

    static HashMap<String, HashSet<AddressDto>> addressHashMap = new HashMap<>(); // key: 로

    void init() throws IOException {
        final File folder = new File(".\\resources\\202211_address");
        final File[] fileList = Objects.requireNonNull(folder.listFiles());

        for(File item : fileList) {
            FileReader fr = new FileReader(item);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                try {
                    // 2: 도/시 3: 시/구/군 4: 동/면 5:리 10:로/길
                    String[] split = line.split("\\|");
                    AddressDto addressDto = new AddressDto(split[2], split[3], split[4], split[5], split[10]);
                    Set<AddressDto> addressList = addressHashMap.get(split[10]);
                    if(addressList != null) {
                        addressList.add(addressDto);
                    } else {
                        HashSet<AddressDto> address = new HashSet<>();
                        address.add(addressDto);
                        addressHashMap.put(split[10], address);
                    }
                } catch (Exception e) {
                    System.out.println(line);
                }
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

    public String[] getRefinedAddress(String input) {
        String calculatedAddress = input;

        ArrayList<Integer> itemIndexes = new ArrayList<>();
        int e = input.indexOf('로');
        if (e != -1) {
            itemIndexes.add(e);

            while(e >= 0) {
                e = input.indexOf('로', e+1);
                itemIndexes.add(e);
            }
        } else {
            e = input.indexOf('길');
            itemIndexes.add(e);

            while(e >= 0) {
                e = input.indexOf('길', e+1);
                itemIndexes.add(e);
            }
        }

        String resultMatchedRaod = "";
        for (Integer nextIndex : itemIndexes) {
            if (nextIndex != -1) {
                String matchedRoad = findMatchRoad(nextIndex, input);
                if(matchedRoad != null) {
                    resultMatchedRaod = matchedRoad;
                }
                HashSet<AddressDto> matchedAddress = findMatchedAddress(matchedRoad);
                if(matchedAddress != null) {
                    Iterator<AddressDto> addressDtoIterator = matchedAddress.iterator();

                    double maxPatchPoint = 0;
                    while (addressDtoIterator.hasNext()) {
                        AddressDto nextAddress = addressDtoIterator.next();
                        double matchPoint = getMatchPoint(nextAddress.toString(), input);
                        if (matchPoint > maxPatchPoint) {
                            maxPatchPoint = matchPoint;
                            calculatedAddress = nextAddress.toString();
                        }
                    }
                }
            }
        }

        return new String[] {resultMatchedRaod, calculatedAddress, input};
    }


    public String findMatchRoad(int index, String input) {
        String tempString = String.valueOf(input.charAt(index));;

        for(int i=index-1; i>0; i--) {
            String stringItem = String.valueOf(input.charAt(i));
            if(isMatches(stringItem)) {
                String concat = stringItem.concat(tempString);
                tempString = concat;
                HashSet<AddressDto> addressDtos = addressHashMap.get(concat);
                if(addressDtos != null) {
                    return tempString;
                }
            }
        }
        return null;
    }

    public HashSet<AddressDto> findMatchedAddress(String addressRoad) {
        HashSet<AddressDto> addressDtos = addressHashMap.get(addressRoad);
        return addressDtos;
    }

    // https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
    public static double getMatchPoint(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    private boolean isMatches(String stringItem) {
        return stringItem.matches(".*[가-힣|0-9]+.*");
    }
}
