set xlabel "Número de etiquetas"
set ylabel "Número total de slots vázio"
set monochrome
set key vertical top left
set grid
set pointsize 2
set terminal png
set yrange [0:1100]
set ytics (0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100)
set output './results/vazio.png'
plot "lowerbound_100_100_1000_2000_64.txt" u 1:4 t 'lowerbound' w linespoints pt 1,"eom-lee_100_100_1000_2000_64.txt" u 1:4 t 'eom-lee' w linespoints pt 2
