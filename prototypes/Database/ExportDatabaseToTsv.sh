echo "select * from Adjacent_Points" | mysql -B -uroot -proot TrainTrax > TrainTraxSampleDb_AdjacentPointsTable.tsv
echo "select * from Track_Block" | mysql -B -uroot -proot TrainTrax > TrainTraxSampleDb_TrackBlockTable.tsv
echo "select * from Track_Point" | mysql -B -uroot -proot TrainTrax > TrainTraxSampleDb_TrackPointTable.tsv

