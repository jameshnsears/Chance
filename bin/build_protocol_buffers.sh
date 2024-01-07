PROTOC_DIR=$PWD

cd ../module/data/src/main/proto

# we generate java classes; generating kotlin requires manually fixing some of the generated code!
$PROTOC_DIR/protoc --java_out=../java *.proto

cd $PROTOC_DIR
