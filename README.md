# Gephi Toolkit - All Gephi in one JAR library
[![build](https://github.com/gephi/gephi-toolkit/actions/workflows/ci.yml/badge.svg)](https://github.com/gephi/gephi-toolkit/actions/workflows/ci.yml)
[![javadoc](https://javadoc.io/badge2/org.gephi/gephi-toolkit/javadoc.svg)](https://javadoc.io/doc/org.gephi/gephi-toolkit)

The [Gephi](http://gephi.org) Toolkit project packages essential Gephi modules (Graph, Layout, Filters, IO…) in a standard Java library. It can be used on a server or command-line tool to do the same things Gephi does, but programmatically.

## Use the toolkit

Best way to start is through examples on [Toolkit Demos](https://github.com/gephi/gephi-toolkit). It shows examples how to use the toolkit. If you need support, the community can help you on [Discussions](https://github.com/gephi/gephi-toolkit/discussions/categories/q-a).

- [Gephi Toolkit Tutorial](http://www.slideshare.net/gephi/gephi-toolkit-tutorialtoolkit)

- [Code examples](https://github.com/gephi/gephi-toolkit-demos)

- [Javadoc](https://www.javadoc.io/doc/org.gephi/gephi-toolkit/latest/index.html)

#### From a Maven project

```xml
<dependency>
    <groupId>org.gephi</groupId>
    <artifactId>gephi-toolkit</artifactId>
    <version>0.10.1</version>
</dependency>
```

#### From a Gradle project

```
compile 'org.gephi:gephi-toolkit:0.10.1'
```

#### From a Scala SBT Project

```
resolvers ++= Seq(
  "gephi-thirdparty" at "https://raw.github.com/gephi/gephi/mvn-thirdparty-repo/"
)

libraryDependencies += "org.gephi" % "gephi-toolkit" % "0.10.1" classifier "all"
```

## Latest releases

### Stable

- Latest stable release on [gephi.org](http://gephi.org/toolkit).

### Development Build

- [gephi-toolkit-0.10.2-SNAPSHOT-all.jar](https://oss.sonatype.org/service/local/artifact/maven/content?r=snapshots&g=org.gephi&a=gephi-toolkit&v=0.10-2-SNAPSHOT&c=all) (Jar)

### Development Build (Maven)

If you use Maven you can directly depend on the latest development version of the toolkit (i.e the -SNAPSHOT version).

- Add the Gephi repository

```xml
<project>
...
   <repositories>
      <repository>
         <id>oss-sonatype</id>
         <name>oss-sonatype</name>
         <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
         <snapshots>
            <enabled>true</enabled>
         </snapshots>
      </repository>
   </repositories>
...
</project>
```

- Add the dependency

```xml
<project>
...
   <dependencies>
      <dependency>
         <groupId>org.gephi</groupId>
         <artifactId>gephi-toolkit</artifactId>
         <version>0.10.2-SNAPSHOT</version>
      </dependency>
      ...
   </dependencies>
...
</project>
```

## Build

The Gephi Toolkit is entirely based on Gephi's source code and packages the core modules in a single JAR. 

It sources its Gephi dependencies from [Maven Central](https://central.sonatype.com/namespace/org.gephi).

### Requirements

- Java JDK 11.

- [Apache Maven](http://maven.apache.org/) version 3.6.3 or later

### Checkout and Build the sources

- Fork the repository and clone

        git clone git@github.com:username/gephi-toolkit.git

- Run the following command or open the project in an IDE like NetBeans or IntelliJ IDEA

        mvn clean install

### Can the Toolkit use plugins?

Yes that is possible if the plug-in doesn’t depend on something not included in the Toolkit, for instance the UI. If that happens, it is likely that the plug-in has been divided in several modules, and in that case one need only the core and can exclude the UI.
Consult this [HowTo](https://github.com/gephi/gephi/wiki/How-to-use-plug-ins-with-the-Toolkit) page to know how to extract the plugin JARs from the NBM file. Once you have the JARs, include them in your project’s classpath, in addition of the Gephi Toolkit.

### Can it depends on a development version of Gephi?

Yes, either a snapshot or a locally built version.

To build it based on your own locally-built Gephi do the following:

- Build Gephi from its own repository normally (`mvn clean install`)
- This should have installed or overwritten all modules artefacts within your local Maven directory, usually `$USERHOME/.m2`
- Rebuild the toolkit, making sure to depend on the Gephi's version you just built

## License

Gephi's source code is distributed under the dual license [CDDL 1.0](http://www.opensource.org/licenses/CDDL-1.0) and [GNU General Public License v3](http://www.gnu.org/licenses/gpl.html). Read the [Legal FAQs](https://gephi.org/legal/faq/)  to learn more.
