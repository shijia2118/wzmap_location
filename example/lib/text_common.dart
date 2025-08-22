import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class TextCommon extends StatelessWidget {
  final String text;
  final Color color;
  final double fontSize;
  final bool bold;
  final bool softWrap; // 是否自动换行
  final bool center;
  final int maxLines;
  final double height;
  final TextDirection textDirection;
  final TextDecoration textDecoration;

  TextCommon(
    this.text, {
    this.color,
    this.fontSize,
    this.bold: false,
    this.softWrap: false,
    this.center: false,
    this.maxLines,
    this.height,
    this.textDirection,
    this.textDecoration,
  });

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      textAlign: center ? TextAlign.center : TextAlign.start,
      maxLines: maxLines,
      overflow: maxLines == null ? TextOverflow.visible : TextOverflow.ellipsis,
      softWrap: softWrap,
      textDirection: textDirection,
      style: TextStyle(
        color: color ?? Colors.black,
        fontSize: fontSize ?? 14,
        fontWeight: bold ? FontWeight.bold : FontWeight.normal,
        height: height ?? 1.4,
        decoration: textDecoration,
      ),
    );
  }
}
