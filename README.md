JPassword scrambler
==========
<a href="https://codeclimate.com/github/hasherezade/jpassword_scrambler/maintainability"><img src="https://api.codeclimate.com/v1/badges/5cf4a2cb333f6e70dc51/maintainability" /></a><br/>
Small utility to generate complicated passwords - version with GUI<br/>
Compatibile with: https://github.com/hasherezade/password_scrambler<br/><br/>
<img src=https://pbs.twimg.com/media/COYFzqKUcAA2_Gx.png></img>

## Mac OS, Windows
Mac or Windows users may experiece Illegal key size exception because the default crypto files shipped with JDK don't provide unlimited file strength.To resolve this issue: <br/>

* Download the appropriate JCE(Java Cryptography Extension) depending on your JDK version:
  * [JDK-6](http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html).
  * [JDK-7](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)
  * [JDK-8](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
* Extract the zip files 
* Move all jars to ${java.home}/jre/lib/security/ i.e. ```/usr/libexec/java_home/jre/lib/security```
