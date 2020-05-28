

1. git clone 
2. mvn clean & mvn install
3. java -jar -r target/api-exam-0.0.1-SNAPSHOT.jar

### api example

#### Development environment
------
<pre>
Java, Spring boot, h2, Maven, IntelliJ, Linux
</pre>

#### How to build & run
------
<pre>
git clone https://github.com/eottabom/api-exam.git
{project.base.path}$ mvn clean & mvn install
{project.base.path}/target/$ java -jar -r target/api-exam-0.0.1-SNAPSHOT.jar
</pre>


#### API Guide
------
<pre>
1. 연도별 금액 많은 고객 추출
/api/customer/max-account

2. 2018년 또는 2019년에 거래가 없는 고객 추출
/api/customer/no-transaction

3. 거래금액 합계 합계금액이 큰 순서로 출력
/api/management/sum-amt

4. 해당 지점의 거래 금액 합계 출력
/api/management/total-transaction/{brName}

</pre>


#### Troubleshooting
------
+ Timeout on blocking read for 5000 MILLISECONDS
    - @AutoConfigureWebTestClient(timeout = "36000")

+ 404 Not found 처리
    - throw new ResponseStatusException 로 예외 처리 

+ etc..
    - csv 파일은 database 스키마를 통해 table을 생성하고, csv 파일을 읽어서 초기 데이터를 init
    - 최초 설계할 때 각 도메인 별로 설계하였으나, Dto Response 객체를 사용
        - 물론 다른 기능들이 있다면, 도메인 설계를 해야하지만, 현 기능에서는 없어도 된다고 판단
    - ATTD 기반으로 Test 코드를 작성하였기 때문에, 별도의 Service layer의 대한 별도 테스트를 하지 않음