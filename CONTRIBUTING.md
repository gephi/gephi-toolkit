# Contributor Guide

This document guides you through the process of contributing to the Gephi Toolkit. It is a living document and will be updated as the project evolves.

As the Gephi Toolkit is just an aggregation of Gephi's modules, it doesn't contain any source code besides integration tests. To directly contribute to the Toolkit's capabilities, you need to contribute to Gephi's modules.

## Integration Tests

Integration tests are a set of Gephi Toolkit tests that mimic real-world applications and use cases. They are located in the [src/test](https://github.com/gephi/gephi-toolkit/tree/master/src/test) folder. You can contribute by extending this set of tests. The more extensive, the more reliable the toolkit will be.

The tests are written in [JUnit](https://github.com/junit-team/junit4) and use the Gephi APIs to execute tasks. If you find a bug, please report it directly on Gephi's [issue tracker](https://github.com/gephi/gephi/issues). Best if you can attach a test case that reproduces the bug.

## Other JVM languages

The Gephi Toolkit is written in Java, but it can be used from other JVM languages like Scala or Kotlin. You can contribute by adding examples in these languages. 

You can get inspired by the existing [Toolkit Demos](https://github.com/gephi/gephi-toolkit-demos) written in Java. This repository is currently only Java but we would like to add examples in other languages so feel free to get started.

## Algorithms

As a researcher or an algorithm practitioner, you can contribute to the Gephi Toolkit by improving the algorithms' robustness, performance, and accuracy. Notably, standard graph algorithms like Connected Components or PageRank are already implemented in the toolkit, but they can be improved. For instance:
* Compare Gephi's implementation with the reference paper and see if the code is correct. Also check if the paper describes parameters not editable in the current implementation.
* Write or improve existing unit tests. Unit tests are essential and should be particularly thorough for algorithms as researchers rely on them for their work.
* Improve the implementation's performance by using more efficient data structures or algorithms. To do that thoroughly, you can use a profiler like [YourKit](https://www.yourkit.com/) or [JMH](https://github.com/openjdk/jmh).
* Improve the code documentation so the implementation is easier to understand and maintain. The best is to use the same notation as the reference paper.
* Compare implementation and corner cases with other graph libraries. While it's not always possible, it's best if all major graph libraries return the same results for the same input. Raise issues on GitHub if you find any discrepancies.

You can also contribute by adding new algorithms to Gephi. A good way to start is to implement a [plugin](https://github.com/gephi/gephi-plugins).

There are various algorithms implement in Gephi:
* Shortest paths are in the [AlgorithmsPlugin](https://github.com/gephi/gephi/tree/master/modules/AlgorithmsPlugin) module.
* Generators are in the [GeneratorPlugin](https://github.com/gephi/gephi/tree/master/modules/GeneratorPlugin) module.
* Layouts are in the [LayoutPlugin](https://github.com/gephi/gephi/tree/master/modules/LayoutPlugin) module.
* Graph algorithms and community detection are in the [StatisticsPlugin](https://github.com/gephi/gephi/tree/master/modules/StatisticsPlugin) module.

An excellent way to start is also to look at the [open issues related to algorithms](https://github.com/gephi/gephi/issues?q=is%3Aopen+is%3Aissue+label%3AStatistics).

## Community support

Engage with the community's questions directly on [GitHub Discussions](https://github.com/gephi/gephi-toolkit/discussions). Any help is appreciated.