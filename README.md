[Diego Martín Tassis](https://twitter.com/diegomtassis) - August 2015

## Getting Started

Sample application built in order to practice a little bit of TDD.

It might be seen as a **Code Kata**.

The idea was to use as less 3rd party libraries as possible.

## Problem Description

###  Train Routing

The problem is a simplification of the [Vehicle routing problem](https://en.wikipedia.org/wiki/Vehicle_routing_problem).

Given a train network described using a graph where the arcs are tracks and vertices are cities:

* The cities are represented by letters [A-Z].
* The tracks are labeled with a distance.
* A track between A and B doesn't imply the existence of a track between B and A.
* If 2 tracks AB and BA exist, they do not need to have the same distance.
* There are cities which are not connected.
* There is not more than 1 track connecting 2 cities. 

Build services in order to:

* Given a route, e.g. ABCDE, check whether it exists in the network and return its distance.
* Calculate all the routes connecting 2 cities (may be the same city) separated by a number of stops between n and m.
* Calculate all the routes connecting 2 cities by a distance lower than n.
* Calculate the shortest route connecting 2 cities. When more than one, return all.  

### Examples

Given the following train network:
AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7, FH10, FG3, GH4, XZ50, XY30, YZ20

Provide the right output for the following scenarios:

##### The distance of the route ABC
Expected output: 9

##### The distance of the route AD
Expected output: 5

##### The distance of the route ADC
Expected output: 13

##### The distance of the route AEBCD
Expected output: 22

##### The distance of the route AED
Expected output: NO SUCH ROUTE

##### The number of trips starting at C and ending at C with a maximum of 3 stops
Expected output: CDC (2 stops), CEBC (3 stops)

##### The number of trips starting at A and ending at C with exactly 4 stops.
Expected output: ABCDC, ADCDC, ADEBC

##### The length of the shortest route (in terms of distance to travel) from A to C.
Expected output: 9

##### The length of the shortest route (in terms of distance to travel) from B to B.
Expected output: 9

##### The number of different routes from C to C with a distance of less than 30.
Expected output: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC


## How to build

### Requirements
* maven 3
* java 8

### Install
Go to the root folder and execute:
```
mvn clean install
```

### Execution

There are some java static invokers created just for demonstration/test purposes.

These invokers are pretty simple and include no validation at all (validation is performed starting at the application layer), so if an integer argument is expected and an invalid value is provided the result will be the exception generated by the invoker, since the real application layer is not even invoked.

This is not the only disadvantage of the invokers. The application is developed in such a fashion that would be rather easy to create a microservice (out of the scope of this exercise, the most simple way would be to use Spring Boot) and expose it via rest. This would allow to take advantage of the fact that the services learn/cache information which make following executions to perform better, which of course doesn't happen when the invokers are invoked isolated.

Another possibility for execution is to create an integration test similar to the ones under /client/src/test/java, load the application services from the DI context and execute. 

The evidence that the application works with the test samples provided within the description is accomplished by executing 
```
com.dmt.train.routing.client.RequiredSampleITest
```
which is located in the client module.

#### Invokers

Move to the /client/target folder and execute:

(If needed, the sample input file under /client/test/resources may be used).

##### Distance of a route
* java -cp client-0.0.1-SNAPSHOT-shaded.jar com.dmt.train.routing.client.DistanceCalculator

##### Number of trips between cities filtered by stops
* java -cp client-0.0.1-SNAPSHOT-shaded.jar com.dmt.train.routing.client.TripsFinderByStops

##### Number of trips between cities filtered by distance
* java -cp client-0.0.1-SNAPSHOT-shaded.jar com.dmt.train.routing.client.TripsFinderByDistance

##### Shortest trip between two cities
* java -cp client-0.0.1-SNAPSHOT-shaded.jar com.dmt.train.routing.client.TripsFinderWithMinimumDistance

## Design

### Project Layout

Standard maven multi module layout.

* **root**: aggregator and parent pom for the submodules.
* **application-api**: abstractions for the application layer
* **domain-api**: abstractions for the domain layer
* **service**: service implementations
* **client**: invokers for the application services and integration tests
* **utils**: general purpose utilities (derived from the commandment of not using 3rd party libraries)

Each module respects the maven standard layout for code, tests and resources.


### Design considerations

##### DDD
DDD approach. Architecture and application roles as suggested by Eric Evans in his book "Domain Driven Design".

##### SOLID use intensively
Not only at the class/component but also at the bundle level (modularization).

##### Others

* Recoverage after validation failures not implemented, so only unchecked exceptions are thrown.

### Implementation considerations

* Spring fwk context library used as dependency injection framework (only in the client module).
* JSR-330 annotations in the service module.
* At some point in time it was needed to choose between an iterative or recursive approach. Chose the iterative approach because a smaller memory footprint, though this is not very important when dealing with a low number of cities (e.g. the test sample). 
* No specific algorithm optimization performed, apart from the usage of a repository for the graph and the already visited routes. Usually this kind of problems with graphs may be optimized by using a caching aspect to avoid solving the same problem more than once. This is easier when a recursive approach is used.

## TDD

####  Testing considerations

* Formatting done following the AAA pattern, but using the notation suggested by Martin Fowler in his article "Mocks aren't stubs".
* Naming of the tests following the suggestions done by Roy Osherove in his book "The art of Unit testing", except for the most-optimistic vanilla cases where I simply use the name of the method under test.
* EasyMock used as mocking framework
* TDD put in practice following a london/mockist approach as explained by Martin Freeman and Nat Pryce in the book "Growing Object-Oriented Software, guided by tests". This is:
 1. Begin with a failing integration test describing an intermediate or final feature.
 2. Identify responsibilities and create interfaces.
 3. Implement interfaces and add unit tests for the implemented code (TFD).
 4. The initial test gets green.
 5. Refactor.

####  Features TODO

The integration tests created as driver for the TDD process are located inside the **client** module.

##### Import graph
* Register a 2 point route. Done
* Register a 3 point route: invalid operation. Done
* Import a graph from a file. Done

##### Distance of a route
* Get the distance of a 2 point existing route. Done
* Get the distance of a 2 point not existing route: null distance. Done
* Get the distance of a empty route: expected illegal argument. Done
* Get the distance of a route with invalid characters: expected illegal argument. Done
* Get the distance of a 5 edge route. Done
* Get the distance of a 5 edge non existing route: null distance. Done

##### Number of trips between cities filtered by stops
* Get the trips between 2 points separated exactly by one stop, and the route exists. Done
* Get the trips between 2 points separated exactly by one stop, and the route does not exist. Done
* Get the trips between A and C separated exactly by 4 stops. Done

##### Number of trips between cities filtered by distance
* Get the trips between C and C separated exactly by a distance lower than 30. Done
* Get the trips between C and C separated exactly by a distance lower than 5. Does not exist. Done

##### Shortest trip between two cities
* Get the shortest trip between 2 cities when there is a direct route between then and it is the shortest route. Done
* Get the shortest trip between 2 cities when there is a multi-stop route shorter than a direct route between them. Done
* Get the shortest trip between 2 cities when there is not a route between them. Done
* Get the shortest trip between 2 cities when there is 2 routes with the same distance. Done
