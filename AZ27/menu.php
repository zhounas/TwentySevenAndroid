<?php
$dbc=mysqli_connect("localhost","root","root","twentyseven");

if(isset($_GET['android_id']) AND isset($_GET['querry'])){
	$android_id=$_GET['android_id'];
	$querry=$_GET['querry'];
	$result=mysqli_query($dbc,"SELECT * FROM utilisateurs WHERE android_id= '$android_id'");
	if(mysqli_num_rows($result)==1){
		$row=mysqli_fetch_assoc($result);
		if($row['status']==1){
			if($querry == "0"){
				$result=mysqli_query($dbc,"SELECT * FROM familles");
				if(mysqli_num_rows($result)>0){
					echo mysqli_num_rows($result);
					while($row=mysqli_fetch_assoc($result)){
						$famille=$row['famille'];
						$id_famille=$row['id'];
						echo ",$famille";
						echo ":$id_famille";
					}
				}else{echo "0,Error";}
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
				}else{echo "0,Error";}
			}

		}else{
			echo"Not connected";
		}	
	}else{
		echo"Not connected";
	}
}
?>