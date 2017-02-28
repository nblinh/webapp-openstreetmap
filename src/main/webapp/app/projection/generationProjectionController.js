var app = angular.module('generationProjectionController', ['menuService', 'generationService']);
app.controller('generationProjectionController', function($scope, $rootScope, getGenerationProjectionInfo, generationProjectionCalibration){

    $scope.init = function(){
        $scope.generationObject=new Object();
        $scope.generationObject.name="";

        getGenerationProjectionInfo.charge({},function(data){
            $scope.zoningList = data.zoningList;
            $scope.generationObject.zoningId=Object.keys(data.zoningList)[0];

            $scope.zonalDatas = data.zonalDatas;
            $scope.generationObject.zonalData=$scope.zonalDatas[0];

            $scope.calibrations = data.generationCalibrations;
            $scope.generationObject.calibration=$scope.calibrations[0];

            $scope.travelDatas = data.travelDatas;
            $scope.generationObject.travelData=$scope.travelDatas[0];
        });
    }

    $scope.generate = function (){
        generationProjectionCalibration.charge({'name':$scope.generationObject.name, 'zoningId':$scope.generationObject.zoningId,
            'zonalDataName':$scope.generationObject.zonalData, 'calibrationName':$scope.generationObject.calibration},function(data){
            if(data.operation=="sucess"){
                $rootScope.projections.push($scope.generationObject.name);
                $scope.generationObject.name="";
                $('#updateSucess').show();
            }else{
                $('#updateFail').show();
            }
            $(".alert-box").fadeOut(3000);
        });
        $('#myModal').foundation('reveal', 'close');
    }
});