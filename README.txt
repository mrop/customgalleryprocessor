Introduction
============

This project contains an alternative way to scale images. The default functionality is described on http://www.onehippo.org/library/concepts/images-and-assets/create-a-custom-image-set.html. The alternative way the images are scaled in this project takes into account the aspect ratios of the original and the bounding box. 

The alternative way is best shown by checking out the project and have a look at the examples.

However the formal description is as follows ( with curtosy to Owen Knoote (o.knoote@onehippo.com )).

Imagine Scaling an image, I_original for a boundingbox B to an scaled image, I_scaled.

If the aspect ratio of I_original is smaller that the aspect ration of B, the image is scale so that the width of B equals the with of I_scaled. The image will be centered vertically and the lower and upper part will be cut off.

If the aspect ratios of I_original and B are equal, the image will only be scaled.

If the aspect ration of I_original is greater then B, the image will be scaled so that the height of B equals the height of I_scaled. The image will be centered vertically and the left and right part will be cut off.



Running locally
===============

This project uses the Maven Cargo plugin to run Essentials, the CMS and site locally in Tomcat.
From the project root folder, execute:

  mvn clean verify
  mvn -P cargo.run

Access the Hippo Essentials at http://localhost:8080/essentials.
After your project is set up, access the CMS at http://localhost:8080/cms and the site at http://localhost:8080/site.
Access the presentation at http://localhost:8080/demo.
Logs are located in target/tomcat7x/logs

Building distribution
=====================

To build a Tomcat distribution tarball containing only deployable artifacts:

  mvn clean verify
  mvn -P dist

See also src/main/assembly/distribution.xml if you need to customize the distribution.

Using JRebel
============

Set the environment variable REBEL_HOME to the directory containing jrebel.jar.

Build with:

  mvn clean verify -Djrebel

Start with:

  mvn -P cargo.run -Djrebel

Best Practice for development
=============================

Use the option -Drepo.path=/some/path/to/repository during start up. This will avoid
your repository to be cleared when you do a mvn clean.

For example start your project with:

  mvn -P cargo.run -Drepo.path=/home/usr/tmp/repo

or with jrebel:

  mvn -P cargo.run -Drepo.path=/home/usr/tmp/repo -Djrebel

Hot deploy
==========

To hot deploy, redeploy or undeploy the CMS or site:

  cd cms (or site)
  mvn cargo:redeploy (or cargo:undeploy, or cargo:deploy)

Automatic Export
================

Automatic export of repository changes to the filesystem is turned on by default. To control this behavior, log into
http://localhost:8080/cms/console and press the "Enable/Disable Auto Export" button at the top right. To set this
as the default for your project edit the file
./bootstrap/configuration/src/main/resources/configuration/modules/autoexport-module.xml

Monitoring with JMX Console
===========================
You may run the following command:

  jconsole

Now open the local process org.apache.catalina.startup.Bootstrap start
