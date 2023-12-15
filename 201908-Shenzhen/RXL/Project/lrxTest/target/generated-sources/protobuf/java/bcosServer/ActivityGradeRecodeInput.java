// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: connect.proto

package bcosServer;

/**
 * <pre>
 *活动成绩记录
 * </pre>
 *
 * Protobuf type {@code ActivityGradeRecodeInput}
 */
public  final class ActivityGradeRecodeInput extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:ActivityGradeRecodeInput)
    ActivityGradeRecodeInputOrBuilder {
  // Use ActivityGradeRecodeInput.newBuilder() to construct.
  private ActivityGradeRecodeInput(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private ActivityGradeRecodeInput() {
    actID_ = 0;
    stuID_ = 0L;
    actName_ = "";
    stuName_ = "";
    extInfo_ = "";
    time_ = 0L;
    actSignature_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ActivityGradeRecodeInput(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            actID_ = input.readUInt32();
            break;
          }
          case 16: {

            stuID_ = input.readUInt64();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            actName_ = s;
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            stuName_ = s;
            break;
          }
          case 42: {
            java.lang.String s = input.readStringRequireUtf8();

            extInfo_ = s;
            break;
          }
          case 48: {

            time_ = input.readUInt64();
            break;
          }
          case 58: {
            java.lang.String s = input.readStringRequireUtf8();

            actSignature_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return bcosServer.Connect.internal_static_ActivityGradeRecodeInput_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return bcosServer.Connect.internal_static_ActivityGradeRecodeInput_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            bcosServer.ActivityGradeRecodeInput.class, bcosServer.ActivityGradeRecodeInput.Builder.class);
  }

  public static final int ACTID_FIELD_NUMBER = 1;
  private int actID_;
  /**
   * <code>optional uint32 ActID = 1;</code>
   */
  public int getActID() {
    return actID_;
  }

  public static final int STUID_FIELD_NUMBER = 2;
  private long stuID_;
  /**
   * <code>optional uint64 StuID = 2;</code>
   */
  public long getStuID() {
    return stuID_;
  }

  public static final int ACTNAME_FIELD_NUMBER = 3;
  private volatile java.lang.Object actName_;
  /**
   * <code>optional string ActName = 3;</code>
   */
  public java.lang.String getActName() {
    java.lang.Object ref = actName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      actName_ = s;
      return s;
    }
  }
  /**
   * <code>optional string ActName = 3;</code>
   */
  public com.google.protobuf.ByteString
      getActNameBytes() {
    java.lang.Object ref = actName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      actName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int STUNAME_FIELD_NUMBER = 4;
  private volatile java.lang.Object stuName_;
  /**
   * <code>optional string StuName = 4;</code>
   */
  public java.lang.String getStuName() {
    java.lang.Object ref = stuName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      stuName_ = s;
      return s;
    }
  }
  /**
   * <code>optional string StuName = 4;</code>
   */
  public com.google.protobuf.ByteString
      getStuNameBytes() {
    java.lang.Object ref = stuName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      stuName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int EXTINFO_FIELD_NUMBER = 5;
  private volatile java.lang.Object extInfo_;
  /**
   * <code>optional string ExtInfo = 5;</code>
   */
  public java.lang.String getExtInfo() {
    java.lang.Object ref = extInfo_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      extInfo_ = s;
      return s;
    }
  }
  /**
   * <code>optional string ExtInfo = 5;</code>
   */
  public com.google.protobuf.ByteString
      getExtInfoBytes() {
    java.lang.Object ref = extInfo_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      extInfo_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TIME_FIELD_NUMBER = 6;
  private long time_;
  /**
   * <code>optional uint64 Time = 6;</code>
   */
  public long getTime() {
    return time_;
  }

  public static final int ACTSIGNATURE_FIELD_NUMBER = 7;
  private volatile java.lang.Object actSignature_;
  /**
   * <code>optional string ActSignature = 7;</code>
   */
  public java.lang.String getActSignature() {
    java.lang.Object ref = actSignature_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      actSignature_ = s;
      return s;
    }
  }
  /**
   * <code>optional string ActSignature = 7;</code>
   */
  public com.google.protobuf.ByteString
      getActSignatureBytes() {
    java.lang.Object ref = actSignature_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      actSignature_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (actID_ != 0) {
      output.writeUInt32(1, actID_);
    }
    if (stuID_ != 0L) {
      output.writeUInt64(2, stuID_);
    }
    if (!getActNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 3, actName_);
    }
    if (!getStuNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 4, stuName_);
    }
    if (!getExtInfoBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 5, extInfo_);
    }
    if (time_ != 0L) {
      output.writeUInt64(6, time_);
    }
    if (!getActSignatureBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 7, actSignature_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (actID_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(1, actID_);
    }
    if (stuID_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, stuID_);
    }
    if (!getActNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(3, actName_);
    }
    if (!getStuNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(4, stuName_);
    }
    if (!getExtInfoBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(5, extInfo_);
    }
    if (time_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(6, time_);
    }
    if (!getActSignatureBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(7, actSignature_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof bcosServer.ActivityGradeRecodeInput)) {
      return super.equals(obj);
    }
    bcosServer.ActivityGradeRecodeInput other = (bcosServer.ActivityGradeRecodeInput) obj;

    boolean result = true;
    result = result && (getActID()
        == other.getActID());
    result = result && (getStuID()
        == other.getStuID());
    result = result && getActName()
        .equals(other.getActName());
    result = result && getStuName()
        .equals(other.getStuName());
    result = result && getExtInfo()
        .equals(other.getExtInfo());
    result = result && (getTime()
        == other.getTime());
    result = result && getActSignature()
        .equals(other.getActSignature());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + ACTID_FIELD_NUMBER;
    hash = (53 * hash) + getActID();
    hash = (37 * hash) + STUID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getStuID());
    hash = (37 * hash) + ACTNAME_FIELD_NUMBER;
    hash = (53 * hash) + getActName().hashCode();
    hash = (37 * hash) + STUNAME_FIELD_NUMBER;
    hash = (53 * hash) + getStuName().hashCode();
    hash = (37 * hash) + EXTINFO_FIELD_NUMBER;
    hash = (53 * hash) + getExtInfo().hashCode();
    hash = (37 * hash) + TIME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTime());
    hash = (37 * hash) + ACTSIGNATURE_FIELD_NUMBER;
    hash = (53 * hash) + getActSignature().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static bcosServer.ActivityGradeRecodeInput parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static bcosServer.ActivityGradeRecodeInput parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static bcosServer.ActivityGradeRecodeInput parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(bcosServer.ActivityGradeRecodeInput prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   *活动成绩记录
   * </pre>
   *
   * Protobuf type {@code ActivityGradeRecodeInput}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:ActivityGradeRecodeInput)
      bcosServer.ActivityGradeRecodeInputOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return bcosServer.Connect.internal_static_ActivityGradeRecodeInput_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return bcosServer.Connect.internal_static_ActivityGradeRecodeInput_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              bcosServer.ActivityGradeRecodeInput.class, bcosServer.ActivityGradeRecodeInput.Builder.class);
    }

    // Construct using bcosServer.ActivityGradeRecodeInput.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      actID_ = 0;

      stuID_ = 0L;

      actName_ = "";

      stuName_ = "";

      extInfo_ = "";

      time_ = 0L;

      actSignature_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return bcosServer.Connect.internal_static_ActivityGradeRecodeInput_descriptor;
    }

    public bcosServer.ActivityGradeRecodeInput getDefaultInstanceForType() {
      return bcosServer.ActivityGradeRecodeInput.getDefaultInstance();
    }

    public bcosServer.ActivityGradeRecodeInput build() {
      bcosServer.ActivityGradeRecodeInput result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public bcosServer.ActivityGradeRecodeInput buildPartial() {
      bcosServer.ActivityGradeRecodeInput result = new bcosServer.ActivityGradeRecodeInput(this);
      result.actID_ = actID_;
      result.stuID_ = stuID_;
      result.actName_ = actName_;
      result.stuName_ = stuName_;
      result.extInfo_ = extInfo_;
      result.time_ = time_;
      result.actSignature_ = actSignature_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof bcosServer.ActivityGradeRecodeInput) {
        return mergeFrom((bcosServer.ActivityGradeRecodeInput)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(bcosServer.ActivityGradeRecodeInput other) {
      if (other == bcosServer.ActivityGradeRecodeInput.getDefaultInstance()) return this;
      if (other.getActID() != 0) {
        setActID(other.getActID());
      }
      if (other.getStuID() != 0L) {
        setStuID(other.getStuID());
      }
      if (!other.getActName().isEmpty()) {
        actName_ = other.actName_;
        onChanged();
      }
      if (!other.getStuName().isEmpty()) {
        stuName_ = other.stuName_;
        onChanged();
      }
      if (!other.getExtInfo().isEmpty()) {
        extInfo_ = other.extInfo_;
        onChanged();
      }
      if (other.getTime() != 0L) {
        setTime(other.getTime());
      }
      if (!other.getActSignature().isEmpty()) {
        actSignature_ = other.actSignature_;
        onChanged();
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      bcosServer.ActivityGradeRecodeInput parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (bcosServer.ActivityGradeRecodeInput) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int actID_ ;
    /**
     * <code>optional uint32 ActID = 1;</code>
     */
    public int getActID() {
      return actID_;
    }
    /**
     * <code>optional uint32 ActID = 1;</code>
     */
    public Builder setActID(int value) {
      
      actID_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional uint32 ActID = 1;</code>
     */
    public Builder clearActID() {
      
      actID_ = 0;
      onChanged();
      return this;
    }

    private long stuID_ ;
    /**
     * <code>optional uint64 StuID = 2;</code>
     */
    public long getStuID() {
      return stuID_;
    }
    /**
     * <code>optional uint64 StuID = 2;</code>
     */
    public Builder setStuID(long value) {
      
      stuID_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional uint64 StuID = 2;</code>
     */
    public Builder clearStuID() {
      
      stuID_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object actName_ = "";
    /**
     * <code>optional string ActName = 3;</code>
     */
    public java.lang.String getActName() {
      java.lang.Object ref = actName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        actName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string ActName = 3;</code>
     */
    public com.google.protobuf.ByteString
        getActNameBytes() {
      java.lang.Object ref = actName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        actName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string ActName = 3;</code>
     */
    public Builder setActName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      actName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string ActName = 3;</code>
     */
    public Builder clearActName() {
      
      actName_ = getDefaultInstance().getActName();
      onChanged();
      return this;
    }
    /**
     * <code>optional string ActName = 3;</code>
     */
    public Builder setActNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      actName_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object stuName_ = "";
    /**
     * <code>optional string StuName = 4;</code>
     */
    public java.lang.String getStuName() {
      java.lang.Object ref = stuName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        stuName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string StuName = 4;</code>
     */
    public com.google.protobuf.ByteString
        getStuNameBytes() {
      java.lang.Object ref = stuName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        stuName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string StuName = 4;</code>
     */
    public Builder setStuName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      stuName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string StuName = 4;</code>
     */
    public Builder clearStuName() {
      
      stuName_ = getDefaultInstance().getStuName();
      onChanged();
      return this;
    }
    /**
     * <code>optional string StuName = 4;</code>
     */
    public Builder setStuNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      stuName_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object extInfo_ = "";
    /**
     * <code>optional string ExtInfo = 5;</code>
     */
    public java.lang.String getExtInfo() {
      java.lang.Object ref = extInfo_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        extInfo_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string ExtInfo = 5;</code>
     */
    public com.google.protobuf.ByteString
        getExtInfoBytes() {
      java.lang.Object ref = extInfo_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        extInfo_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string ExtInfo = 5;</code>
     */
    public Builder setExtInfo(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      extInfo_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string ExtInfo = 5;</code>
     */
    public Builder clearExtInfo() {
      
      extInfo_ = getDefaultInstance().getExtInfo();
      onChanged();
      return this;
    }
    /**
     * <code>optional string ExtInfo = 5;</code>
     */
    public Builder setExtInfoBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      extInfo_ = value;
      onChanged();
      return this;
    }

    private long time_ ;
    /**
     * <code>optional uint64 Time = 6;</code>
     */
    public long getTime() {
      return time_;
    }
    /**
     * <code>optional uint64 Time = 6;</code>
     */
    public Builder setTime(long value) {
      
      time_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional uint64 Time = 6;</code>
     */
    public Builder clearTime() {
      
      time_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object actSignature_ = "";
    /**
     * <code>optional string ActSignature = 7;</code>
     */
    public java.lang.String getActSignature() {
      java.lang.Object ref = actSignature_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        actSignature_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string ActSignature = 7;</code>
     */
    public com.google.protobuf.ByteString
        getActSignatureBytes() {
      java.lang.Object ref = actSignature_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        actSignature_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string ActSignature = 7;</code>
     */
    public Builder setActSignature(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      actSignature_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string ActSignature = 7;</code>
     */
    public Builder clearActSignature() {
      
      actSignature_ = getDefaultInstance().getActSignature();
      onChanged();
      return this;
    }
    /**
     * <code>optional string ActSignature = 7;</code>
     */
    public Builder setActSignatureBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      actSignature_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:ActivityGradeRecodeInput)
  }

  // @@protoc_insertion_point(class_scope:ActivityGradeRecodeInput)
  private static final bcosServer.ActivityGradeRecodeInput DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new bcosServer.ActivityGradeRecodeInput();
  }

  public static bcosServer.ActivityGradeRecodeInput getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ActivityGradeRecodeInput>
      PARSER = new com.google.protobuf.AbstractParser<ActivityGradeRecodeInput>() {
    public ActivityGradeRecodeInput parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ActivityGradeRecodeInput(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ActivityGradeRecodeInput> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ActivityGradeRecodeInput> getParserForType() {
    return PARSER;
  }

  public bcosServer.ActivityGradeRecodeInput getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

