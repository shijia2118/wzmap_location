
import 'package:flutter/material.dart';
import 'package:wzmap_location/wayz_models.dart';
import 'package:wzmap_location/wzmap_location.dart';
import 'package:wzmap_location_example/sp_util.dart';
import 'package:wzmap_location_example/text_common.dart';

class LocationPage extends StatefulWidget {
  final String address;
  LocationPage(this.address);

  @override
  _LocationPageState createState() => _LocationPageState();
}

class _LocationPageState extends State<LocationPage> {
  TextEditingController _textEditingController = TextEditingController();
  FocusNode _focusNode = new FocusNode();

  WzMapController controller;
  String address;
  // ReGeocode reGeocode;
  bool _showSearchResultField = false; //是否显示搜索结果
  List<Map<String, dynamic>> searchResultList = []; //搜索结果

  static double searchBarHeight = 35; //搜索框高度

  @override
  void initState() {
    // :TODO: implement initState
    super.initState();
    _focusNode.addListener(_focusNodeListener);

    address = widget.address;

    _textEditingController.addListener(() {
      _fetchInputTips(_textEditingController.text);
    });
  }


  ///关键词搜索地理位置
  _fetchInputTips(String inputText) async {
    searchResultList.clear();
    if (inputText != null && inputText.isNotEmpty) {

     await WzmapLocation.startLocation(onLocation: (location) async {
       String _city = location.city;

       // var res = await WzmapLocation.searchPoi(keyword: inputText,city: _city);
       var res = await WzmapLocation.nearbySearch(keywords: inputText,latitude: location.latitude,longitude: location.longitude,);

       for (var r in res) {

         print('>>>>>>>>${r.name}');

         final point = r.geoPoint;

         if(point == null || point.isEmpty) continue;

         final geo = point.split(',');

         if(geo.length == 2){

           final latitudeStr = geo[0];

           final longitudeStr = geo[1];

           final latitude = double.tryParse(latitudeStr);

           final longitude = double.tryParse(longitudeStr);

           if(latitude == null || longitude == null) continue;

           searchResultList.add({
             'title': r.name,
             "subtitle": r.address ,
             "latLng":LatLng(latitude, longitude),
           });
         }
       }
      }, onError: (error){
       print('>>>>>>error:$error');

     });
    }
    if (mounted) setState(() {});
  }

  ///聚焦时显示搜索页;失焦时显示地图页
  Future<Null> _focusNodeListener() async {
    if (_focusNode.hasFocus && !_showSearchResultField) {
      setState(() {
        _showSearchResultField = true;
      });
    } else {
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: TextCommon('选择地址'),),
      body: Stack(
        children: [
          Column(
            children: [
              Row(
                children: [
                  ///搜索框
                  Expanded(child: Container(
                    width: 311,
                    margin: EdgeInsets.only(left: 15),
                    child: TextField(
                      controller: _textEditingController,
                      maxLines: 1,
                      focusNode: _focusNode,
                      onSubmitted: (value) async {
                        _focusNode.unfocus();
                      },
                    ),
                  ),),

                  ///搜索按钮
                  GestureDetector(
                    onTap: () {
                      _focusNode.unfocus();
                    },
                    child: Container(
                      height: 35,
                      width: 30,
                      color: Colors.transparent,
                      margin: EdgeInsets.only(left: 10),
                      child: Center(
                        child: TextCommon('搜索',
                            fontSize: 12, color: Colors.black),
                      ),
                    ),
                  )
                ],
              ),

              ///当前定位
              Container(
                margin: EdgeInsets.only(left: 15, top: 13),
                child: Row(
                  children: [
                    TextCommon('当前定位',
                        fontSize: 12, color: Colors.white38),
                  ],
                ),
              ),

              ///重新定位
              Container(
                margin: EdgeInsets.only(
                    left: 15, right: 15, top: 10, bottom: 15),
                child: RefreshAddress(address: address),
              ),

              ///地图
              Expanded(
                child: WzMapView(
                  onMapCreated: (c) async {
                    controller = c;

                    await WzmapLocation.startLocation(onLocation: (location){

                      final latLng = LatLng(location.latitude,location.longitude);

                      _mapReMarked(latLng);
                    }, onError: (error){

                    },);
                  },

                  onMapClicked: (latLng) async {
                    //清空marker数组
                    await controller.clearMarkers();
                    print(latLng.longitude);
                    print(latLng.latitude);

                    _mapReMarked(latLng);

                    final uuid = SpUtil.getUuid();

                    WayzResponse wayzRes = await WzmapLocation.reverseGeocode(latitude: latLng.latitude, longitude: latLng.longitude, uuid: uuid);

                    setState(() {
                      address = wayzRes.location.address.name;
                    });
                    print(wayzRes.location.address.name);
                  },

                ),

              ),
            ],
          ),
          Visibility(
            visible: _showSearchResultField,
            child: Container(
              color: Colors.white,
              margin: EdgeInsets.only(top: searchBarHeight),
              child: ListView.builder(
                itemCount: searchResultList.length,
                itemBuilder: (BuildContext context, int index) {
                  String address = searchResultList[index]['title'];
                  String pro = searchResultList[index]['subtitle'];
                  LatLng latLng = searchResultList[index]['latLng'];

                  return SearchResultItem(
                      title: address,
                      subtitle: pro,
                      onTap: () {

                      });
                },
              ),
            ),
          )
        ],
      ),
    );
  }

  ///设置中心点并标记地图
  _mapReMarked(LatLng latLng) async {
    //设置中心点
    await controller.setCenterCoordinate(latLng);
    //标记中心点
    await controller?.addMarker(latLng);
  }
}

class RefreshAddress extends StatelessWidget {
  RefreshAddress({
    Key key,
    @required this.address,

  }) : super(key: key);

  final String address;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Container(
            width: 250,
            child: TextCommon(address ?? '123',
                fontSize: 12, bold: true, softWrap: true, maxLines: 1),
          ),
          GestureDetector(
            onTap: () async {
              await WzmapLocation.startLocation(onLocation: (loc){
                print('>>>>>>>>>>loc:${loc.address}');
              }, onError: (error){},);
              Navigator.pop(context);
            },
            child: Container(
              color: Colors.transparent,
              child: Row(
                children: [
                  Container(
                    margin: EdgeInsets.only(left: 4),
                    child: TextCommon('重新定位',
                        fontSize: 12, color:Colors.blue),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}



///搜索结果item组建
class SearchResultItem extends StatelessWidget {
  final String title;
  final String subtitle;
  final Function onTap;
  const SearchResultItem({
    Key key,
    this.subtitle,
    this.title,
    this.onTap,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: MediaQuery.of(context).size.width,
        color: Colors.transparent,
        padding: EdgeInsets.symmetric(horizontal: 15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: 15),
            TextCommon(title ?? '',
                fontSize: 14, bold: true, color: Colors.black),
            SizedBox(height: 4),
            TextCommon(subtitle ?? '',
                fontSize: 12, color:Colors.white38),
            SizedBox(height: 15),
          ],
        ),
      ),
    );
  }
}
