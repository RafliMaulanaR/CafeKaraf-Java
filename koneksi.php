<?php 

	$con = mysqli_connect("localhost","root","","db_cafe");

	class struk{
    
    public function queryTampil($query){
		global $con;
		$result = mysqli_query($con,$query);
		$rows = [];
		while ($row = mysqli_fetch_assoc($result)) {
			$rows[] = $row;
		}
		return $rows ;
	}
	 public function selectWhere($table,$where,$whereValues){
        global $con;

        $sql = "SELECT * FROM $table WHERE $where ='$whereValues'";
        $query = mysqli_query($con,$sql);
        return $row = mysqli_fetch_assoc($query);
    }
     public function selectSumWhere($table,$namaField,$berinama,$whereValues){
        global $con;

        $result = "SELECT SUM($namaField) as $berinama FROM $table WHERE $whereValues";
        $query = mysqli_query($con,$result);
        return $data = mysqli_fetch_assoc($query);
    }
     public function selectSum($table,$namaField,$berinama){
        global $con;

        $result = "SELECT SUM($namaField) as $berinama FROM $table";
        $query = mysqli_query($con,$result);
        return $data = mysqli_fetch_assoc($query);
    }
	}
 ?>