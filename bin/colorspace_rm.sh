# sudo apt install imagemagick potrace -y
export FROM=BlackHole.jpeg
convert $FROM -colorspace Gray $FROM-bw.jpeg
