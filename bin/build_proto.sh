# NOTE manually extract bin folder from protoc-28.2-linux-x86_64_4.28.2.zip
set -e

PROTOC_DIR=$PWD/bin/
chmod +x $PROTOC_DIR/protoc

cd ../module/data-repo-impl/src/proto

# we generate java classes; generating kotlin requires manually fixing some of the generated code!
$PROTOC_DIR/protoc --java_out=../main/java *.proto

cd $PROTOC_DIR
