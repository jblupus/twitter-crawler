# Twitter Crawler
This is work is part of my masters degree research at the Federal University of Goiás, Institute of Informatics.

This application is a Twitter Crawler, it consumes data from the Twitter through [Twitter API](https://developer.twitter.com/en/docs/api-reference-index)  and stores it in a local [Cassandra](http://cassandra.apache.org) database.

Given a list of ```seed users```, for each ```seed user``` and ```seed user friends``` the Crawler collects the following data:

> [```user friendships```](https://developer.twitter.com/en/docs/accounts-and-users/follow-search-get-users/api-reference/get-friends-ids)
> [```user tweets```](https://developer.twitter.com/en/docs/accounts-and-users/create-manage-lists/api-reference/get-lists-statuses)
> [```user likes```](https://developer.twitter.com/en/docs/tweets/post-and-engage/api-reference/get-favorites-list)


Depends on the following libraries:
> [Spring Context, '5.0.2.RELEASE'](https://github.com/spring-projects/spring-framework)

Additional Java code where created to query data from Cassandra, transform and save it to files to perform quick analysis. 

> [Spring Data Jpa, '2.0.2.RELEASE'](https://projects.spring.io/spring-data-jpa)
> [Spring Data Cassandra, '2.0.2.RELEASE'](https://github.com/spring-projects/spring-data-cassandra/tree/master/spring-data-cassandra)
> [Hibernate Core, '5.2.12.Final'](http://hibernate.org/orm)
> [Twitter4j version = '4.0.6'](twitter4j.org)
> [Gson version, '2.8.2'](https://mvnrepository.com/artifact/com.google.code.gson/gson)

### This code has not been updated since 2017.