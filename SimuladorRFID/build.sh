mkdir bin
rm -r bin/*

for file in ./src/simulador/*; do
  echo $file
  javac -d bin/ -cp src src/simulador/$(basename $file)
done
