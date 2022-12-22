import java.io.IOException;

public class Solution {
    public static void main(String[] args) throws IOException {
        AddressService addressService = new AddressService();
        addressService.init();
        addressService.getRefinedAddress("성남, 분당 백 현 로 265, 푸른마을 아파트로 보내주세요!!");
    }
}
