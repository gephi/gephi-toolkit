# Gephi Toolkit - All Gephi in one JAR library

The [Gephi](http://gephi.org) Toolkit project packages essential Gephi modules (Graph, Layout, Filters, IO…) in a standard Java library which any Java project can use for getting things done. It can be used on a server or command-line tool to do the same things Gephi does but automatically.

## Use the toolkit

Find documentation and examples on the [**Toolkit Portal**](https://github.com/gephi/gephi/wiki/Toolkit). It shows examples how to use the toolkit. Find more help on the [forum](http://gephi.org/plugins), the community can help you.

- [Gephi Toolkit Tutorial](http://www.slideshare.net/gephi/gephi-toolkit-tutorialtoolkit)

- [Code examples](https://github.com/gephi/gephi/wiki/Toolkit)

## Latest releases

### Stable

- Latest stable release on [gephi.org](http://gephi.org/toolkit).

### Nightly builds (0.9-SNAPSHOT)

- [gephi-toolkit-0.9-SNAPSHOT-all.jar](http://nexus.gephi.org/nexus/service/local/artifact/maven/content?r=snapshots&g=org.gephi&a=gephi-toolkit&v=0.9-SNAPSHOT&c=all) (Jar)

- [gephi-toolkit-0.9-SNAPSHOT-javadoc.jar](http://nexus.gephi.org/nexus/service/local/artifact/maven/redirect?r=snapshots&g=org.gephi&a=gephi-toolkit&v=0.9-SNAPSHOT&c=javadoc) (Javadoc)

### Maven

If you have Maven you can directly depend on the latest version of the toolkit

- Add the Gephi repository

        <project>
        ...
           <repositories>
              <repository>
                 <id>gephi-snapshots</id>
                 <name>Gephi Snapshots</name>
                 <url>http://nexus.gephi.org/nexus/content/repositories/snapshots/</url>
              </repository>
              <repository>
                 <id>gephi-releases</id>
                 <name>Gephi Releases</name>
                 <url>http://nexus.gephi.org/nexus/content/repositories/releases/</url>
              </repository>
              ...
           </repositories>
        ...
        </project>

- Add the dependency

        <project>
        ...
           <dependencies>
              <dependency>
                 <groupId>org.gephi</groupId>
                 <artifactId>gephi-toolkit</artifactId>
                 <version>0.9-SNAPSHOT</version>
              </dependency>
              ...
           </dependencies>
        ...
        </project>

## Build

The Gephi Toolkit is entirely based on Gephi's source code and packages the core modules in a single JAR.

### Requirements

- Java JDK 6 or 7 with preferably [Oracle Java JDK](http://java.com/en/).

- [Apache Maven](http://maven.apache.org/) version 3.0.3 or later

### Checkout and Build the sources

- Fork the repository and clone

        git clone git@github.com:username/gephi-toolkit.git

- Run the following command or open the project in Netbeans

        mvn clean install

### Can the Toolkit use plugins?

Yes that is possible if the plug-in doesn’t depend on something not included in the Toolkit, for instance the UI. If that happens, it is likely that the plug-in has been divided in several modules, and in that case one need only the core and can exclude the UI.
Consult this [HowTo](https://github.com/gephi/gephi/wiki/How-to-use-plug-ins-with-the-Toolkit) page to know how to extract the plugin JARs from the NBM file. Once you have the JARs, include them in your project’s classpath, in addition of the Gephi Toolkit.

## License

Gephi main source code is distributed under the dual license [CDDL 1.0](http://www.opensource.org/licenses/CDDL-1.0) and [GNU General Public License v3](http://www.gnu.org/licenses/gpl.html). Read the [Legal FAQs](https://gephi.org/about/legal/faq/)  to learn more.

Copyright 2011 Gephi Consortium. All rights reserved.

The contents of this file are subject to the terms of either the GNU
General Public License Version 3 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://gephi.org/about/legal/license-notice/
or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License files at
/cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 3, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 3] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 3 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 3 code and therefore, elected the GPL
Version 3 license, then the option applies only if the new code is
made subject to such option by the copyright holder.
