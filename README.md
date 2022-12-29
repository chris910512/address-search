# address-search

## 개요
도로명주소를 확인하기 위해서 정부에서 제공하는 도로명 데이터와 비교한다.
로 or 길을 확인하는것이 제일 우선 목표이기 때문에, 로 or 길과 hit 되는 문자열을 발견하면 이 것을 return 한다.
그렇지 않은 경우 임의의 로직으로 추출된 결과를 return 한다.

## 프로젝트 구조
![image](https://user-images.githubusercontent.com/44944612/209044227-ad99e95f-1bce-4477-9465-239cf74a3721.png)

## 제한사항
1. resources 폴더 하위에 최신 도로명주소 데이터가 UTF-8 형식으로 import되어 있어야 한다.
2. 이 프로그램은 사용자가 로 or 길은 어떻게든 정확히 입력했다는 가정 하에 가장 높은 hit ratio를 보인다.
(ex: 태평로인 경우, 태 평 로, 태평 로와 같이 "태평로" 문자열이 순서대로 모두 존재하는 경우에 제일 잘 동작한다.)

## 전체 흐름도 
1. resources directory 하위에 있는 주소 목록 text 파일을 읽어 memory에 올린다.
2. input string에서 로 or 길에 해당하는 부분을 찾는다.
3. 도로명주소와 일치하는 부분이 있는지 확인한다.
4. 일치하는 부분이 있다면 전체 주소를 탐색한다. (도로명주소:전체주소 = 1:N 관계, ex: 신촌로의 경우 여러군데 지자체에 존재함.)

## 개선 필요 사항
1. 실제 운영할 때는 resources directory에서 처리하는 부분을 Redis 등 DB로 이관한다. (파일이 변경될 경우 application을 재기동 해줘야만 함)
2. 도로명주소 import 프로그램 자동화 필요
-- Code Review 후 개선 필요 도출사항
3. 자필 도로명주소 적합성 여부 검증 시 외부 알고리즘 사용 대신 적재해온 도로명주소와 비교해서 처리할 수 있는 방법
4. 자필 도로명주소 Parsing시 Maxlenghth 조건을 비교하여 시간복잡도를 줄일 수 있는 방법
5. 도로명주소에 특수문자가 있는 case에 대한 처리 방법

## 참고사항
* 도로명주소 출처: https://business.juso.go.kr/addrlink/attrbDBDwld/attrbDBDwldList.do?cPath=99MD&menu=%EC%A3%BC%EC%86%8CDB
* 두 문자열의 유사성을 추출하는 알고리즘: https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
