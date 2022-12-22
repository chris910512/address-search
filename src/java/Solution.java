import java.io.IOException;
import java.util.Arrays;

public class Solution {
    public static void main(String[] args) throws IOException {
        AddressService addressService = new AddressService();
        addressService.init();
        System.out.println(Arrays.toString(addressService.getRefinedAddress("성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!")));
        System.out.println(Arrays.toString(addressService.getRefinedAddress("마포구 도화-2길 코끼리분식")));
        System.out.println(Arrays.toString(addressService.getRefinedAddress("핫식스구 비타500로 토레타공원")));

    }
}
