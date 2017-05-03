set xlabel "Número de etiquetas"
set ylabel "Número total de slots em colisão"
set key vertical top left
set grid
set pointsize 2
plot "/Users/rodrigocalegario/Desktop/CIn/siscom/siscomRFID/SimuladorRFID/lowerbound_100_100_1000_2000_64.txt" u 1:3 t 'lowerbound' w linespoints,"/Users/rodrigocalegario/Desktop/CIn/siscom/siscomRFID/SimuladorRFID/eom-lee_100_100_1000_2000_64.txt" u 1:3 t 'eom-lee' w linespoints,