var app = angular.module('projectionODMatrixController', ['menuService', 'generationService', 'blockUI']);
app.controller('projectionODMatrixController', function($scope, $rootScope, blockUI, getProjectionODMatrixInfo, projectionODMatrixDistribution){

    $scope.init = function(){
        $scope.projectionODMatrix=new Object();
        $scope.projectionODMatrix.name="";
        $scope.projectionODMatrix.generation="originalOD";

        getProjectionODMatrixInfo.charge({},function(data){
            $scope.zoningList = data.zoningList;
            $scope.projectionODMatrix.zoningId=Object.keys(data.zoningList)[0];

            $scope.projections=data.generationProjections;
            $scope.projectionODMatrix.projection=data.generationProjections[0];

            $scope.travelDatas=data.travelDatas;
            $scope.projectionODMatrix.travelData=data.travelDatas[0];

            $scope.calibrations = data.distributionCalibrations;
            $scope.projectionODMatrix.calibration=$scope.calibrations[0];

            $scope.utilities = data.utilityFormulation;
            $scope.projectionODMatrix.utility=$scope.utilities[0];
        });
    }

    $scope.test = function(){
    }

    $scope.generate = function (){
        blockUI.start("Projection en cours... Please wait!");

        projectionODMatrixDistribution.charge({'name':$scope.projectionODMatrix.name, 'zoningId':$scope.projectionODMatrix.zoningId,
            'travelDataName':$scope.projectionODMatrix.travelData, 'generation':$scope.projectionODMatrix.generation,
            'projectionName':$scope.projectionODMatrix.projection, 'calibrationName':$scope.projectionODMatrix.calibration,
            'utilityName':$scope.projectionODMatrix.utility},function(data){
            if(data.operation=="sucess"){
                $rootScope.travelDatas.push($scope.projectionODMatrix.name);
                $scope.projectionODMatrix.name="";
                $('#updateSucess').show();
            }else{
                $('#updateFail').show();
            }
            blockUI.stop();
            $(".alert-box").fadeOut(3000);
        });
        $('#myModal').foundation('reveal', 'close');
    }
});