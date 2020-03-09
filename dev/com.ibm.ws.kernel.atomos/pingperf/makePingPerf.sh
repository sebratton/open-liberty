#!/bin/bash
set -xe
rm -rf build/
mkdir -p ./build/temp
rm -rf ../../build.image/wlp/usr/servers/defaultServer/
mkdir -p ../../build.image/wlp/usr/servers/defaultServer/

# copy over server config and feature definition for to prepare for server package
configName=pingPerf_as_war
for x in `find $configName` 
do
  nameWithWLPSubstituted=${x/${configName}/wlp}
  if [ -d $x ]; then
      if [ ! -e ../../build.image/${nameWithWLPSubstituted} ]; then
          mkdir ../../build.image/${nameWithWLPSubstituted}
      fi    
  else       
      rm -f ../../build.image/${nameWithWLPSubstituted}
      cp $x ../../build.image/${nameWithWLPSubstituted}
  fi    
done
../../build.image/wlp/bin/server package defaultServer --archive=`pwd`/build/temp/minifiedPingPerfWlp.zip --include=minify

unzip -q ./build/temp/minifiedPingPerfWlp.zip -x */defaultServer/* -d ./build/
mkdir -p ./build/wlp/usr/servers
minified_wlp=`pwd`/build/wlp
rm -r $minified_wlp/lib/extract
cpl=$minified_wlp/classpath_lib
rm -rf $cpl
mkdir $cpl

set +x
echo populating $cpl ...

echo Copy over files from bundles_needed.txt
for b in `cat ./bundles_needed.txt`
do
    #echo cp $minified_wlp/$b $cpl 
    cp $minified_wlp/$b $cpl 
done

echo copy over bundles of type boot.jar
for b in `cat ./bootJars_needed.txt`
do
    cp $minified_wlp/$b $cpl 
done
set +x

cp ../../cnf/staging/repository/org/apache/felix/org.apache.felix.atomos.runtime/0.0.1.SNAPSHOT/org.apache.felix.atomos.runtime-0.0.1.SNAPSHOT.jar $cpl
cp ../../build.image/wlp/lib/com.ibm.ws.kernel.atomos_*.jar $cpl

rm -f build/wlp/lib/*.jar
rm -rf build/wlp/templates

#export GRAALVM_HOME=/Users/sbratton/Applications/graalvm-ce-java11-19.3.1/Contents/Home
#$GRAALVM_HOME/bin/native-image -cp \
# $minified_wlp/classpath_lib:\
#  -H:Name=subst.pp.build/temp/pingperf/wlp/liberty


cat > ./build/launchPingPerf.sh <<- EOF
	debug=""
	warFile=""
	while test \$# -gt 0
	do
	    case "\$1" in
	        debug) 
	            debug="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000"
	            ;;
	        clean)
	            rm -fr $minified_wlp/usr/servers/defaultServer/workarea
	            echo rm -fr $minified_wlp/usr/servers/defaultServer/workarea
	            ;;
	        war)
	            shift
	            warFile="\$1"
	            ;;     
	    esac
	    shift
	done
	
	set -xe
	cd $minified_wlp
	java \$debug -cp "classpath_lib/*" com.ibm.ws.kernel.atomos.Liberty \$warFile
EOF
chmod a+x ./build/launchPingPerf.sh
echo -e "\n  COMPLETED\n"

echo -e "Launch server with ./build/launchPingPerf.sh\n  URLs: http://localhost:9080/ping/ping/greeting\n        http://localhost:9080/ping/ping/simple"

