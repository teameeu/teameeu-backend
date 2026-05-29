package com.teameau.waymore.careernet.service;

import com.teameau.waymore.careernet.dto.CareerNetTestResponse;

import java.util.List;

final class CareerNetTestCatalog {
    private CareerNetTestCatalog() {
    }

    static List<CareerNetTestResponse> getTests() {
        return List.of(
                new CareerNetTestResponse("30", "직업흥미검사(K) - 중학생", List.of("100206"), "1=3 2=2 3=2 ...", null),
                new CareerNetTestResponse("31", "직업흥미검사(K) - 고등학생", List.of("100207"), "1=3 2=2 3=2 ...", null),
                new CareerNetTestResponse("8", "진로개발준비도검사", List.of("100208", "100209", "100210", "100214", "100215"), "2,2,2,3,3,2,3,...", null),
                new CareerNetTestResponse("9", "이공계전공적합도검사", List.of("100208", "100209", "100210", "100214", "100215"), "2,2,2,3,...", null),
                new CareerNetTestResponse("10", "주요능력효능감검사", List.of("100208", "100209", "100210", "100214", "100215"), "2,2,2,3,...", null),
                new CareerNetTestResponse("19", "진로흥미탐색 - 초등학생", List.of("100205"), "1=5 2=4 3=5 4=5 5=4 ...", null),
                new CareerNetTestResponse("32", "진로개발역량 - 초등학생", List.of("100205"), "1=5 2=4 3=5 4=5 5=4 ...", null),
                new CareerNetTestResponse("20", "직업적성검사 - 중학생", List.of("100206"), "1=5 2=4 3=5 4=5 5=4 ...", null),
                new CareerNetTestResponse("21", "직업적성검사 - 고등학생", List.of("100207"), "1=5 2=4 3=5 4=5 5=4 ...", null),
                new CareerNetTestResponse("35", "진로성숙도검사 - 중학생", List.of("100206"), "1=5 2=4 3=5 4=5 5=4 ... 13=1,8 ...", "13번 문항은 답 2개를 순서대로 입력하며, 답변 8(기타)은 주관식 답변이 필요합니다."),
                new CareerNetTestResponse("36", "진로성숙도검사 - 고등학생", List.of("100207"), "1=5 2=4 3=5 4=5 5=4 ... 13=1,6 ...", "13번 문항은 답 2개를 순서대로 입력하며, 답변 9(기타)은 주관식 답변이 필요합니다."),
                new CareerNetTestResponse("24", "직업가치관검사 - 중학생", List.of("100206"), "1=1 2=4 3=5 ...", null),
                new CareerNetTestResponse("25", "직업가치관검사 - 고등학생", List.of("100207"), "1=1 2=4 3=5 ...", null),
                new CareerNetTestResponse("6", "직업가치관검사 - 일반,대학생", List.of("100208", "100209", "100210", "100214", "100215"), "B1=1 B2=4 B3=5 ...", null),
                new CareerNetTestResponse("26", "진로개발역량검사 - 중학생", List.of("100206"), "1=1 2=4 3=5 ...", null),
                new CareerNetTestResponse("27", "진로개발역량검사 - 고등학생", List.of("100207"), "1=1 2=4 3=5 ...", null),
                new CareerNetTestResponse("37", "진로실행력검사 - 중학생", List.of("100206"), "1=5 2=4 3=5 4=5 5=4 ...", null),
                new CareerNetTestResponse("38", "진로실행력검사 - 고등학생", List.of("100207"), "1=5 2=4 3=5 4=5 5=4 ...", null)
        );
    }
}
