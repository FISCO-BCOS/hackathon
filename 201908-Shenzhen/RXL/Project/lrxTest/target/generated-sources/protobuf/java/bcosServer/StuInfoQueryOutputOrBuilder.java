// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: connect.proto

package bcosServer;

public interface StuInfoQueryOutputOrBuilder extends
    // @@protoc_insertion_point(interface_extends:StuInfoQueryOutput)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional uint64 StuID = 1;</code>
   */
  long getStuID();

  /**
   * <code>optional string StuName = 2;</code>
   */
  java.lang.String getStuName();
  /**
   * <code>optional string StuName = 2;</code>
   */
  com.google.protobuf.ByteString
      getStuNameBytes();

  /**
   * <code>optional string UsName = 3;</code>
   */
  java.lang.String getUsName();
  /**
   * <code>optional string UsName = 3;</code>
   */
  com.google.protobuf.ByteString
      getUsNameBytes();

  /**
   * <code>optional string Major = 4;</code>
   */
  java.lang.String getMajor();
  /**
   * <code>optional string Major = 4;</code>
   */
  com.google.protobuf.ByteString
      getMajorBytes();

  /**
   * <code>optional string ExtInfo = 5;</code>
   */
  java.lang.String getExtInfo();
  /**
   * <code>optional string ExtInfo = 5;</code>
   */
  com.google.protobuf.ByteString
      getExtInfoBytes();

  /**
   * <code>optional uint64 Time = 6;</code>
   */
  long getTime();

  /**
   * <code>optional uint32 UsLevel = 7;</code>
   */
  int getUsLevel();

  /**
   * <code>optional uint32 Grade = 8;</code>
   */
  int getGrade();
}
