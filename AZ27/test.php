<?php
$dbc=mysqli_connect("localhost","root","root","twentyseven");

if(isset($_GET['querry'])){
$querry=$_GET['querry'];
	if($querry == "Familles"){

		$result=mysqli_query($dbc,"SELECT * FROM familles");
		if(mysqli_num_rows($result)>0){
			echo mysqli_num_rows($result);
			while($row=mysqli_fetch_assoc($result)){
				$famille=$row['famille'];
				$id_famille=$row['id'];
				echo ",$famille";
				echo ":$id_famille";
			}
		}
	}elseif(is_numeric($querry)){

		$result=mysqli_query($dbc,"SELECT * FROM elements WHERE id_famille='$querry' ");
		if(mysqli_num_rows($result)>0){
			echo mysqli_num_rows($result);
			while($row=mysqli_fetch_assoc($result)){
				$element=$row['element'];
				$id_element=$row['id'];
				$prix=$row['prix'];
				echo ",$element";
				echo ":$id_element";
				echo ":$prix";
			}
		}
	}
}



?>