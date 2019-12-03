<?php
$dbc=mysqli_connect("localhost","root","root","twentyseven");

if(isset($_GET['android_id']) AND isset($_GET['querry'])){
	$android_id=$_GET['android_id'];
	$querry=$_GET['querry'];
	$result=mysqli_query($dbc,"SELECT * FROM utilisateurs WHERE android_id= '$android_id'");
	if(mysqli_num_rows($result)==1){
		$row=mysqli_fetch_assoc($result);
		$utilisateur=$row['utilisateur'];
		if($row['status']==1){
			if($querry != ""){
				if(mysqli_query($dbc,"INSERT INTO commandes values(null,'$querry','utilisateur',NOW() )")){
					echo "ok ".mysqli_insert_id($dbc)."";	
				}else{echo "Error ".mysqli_error($dbc)."";}
			}else{echo "0,Error";}
		}else{
			echo"Not connected";
		}	
	}else{
		echo"Not connected ";
	}	
}
?>