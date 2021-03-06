USE [TN_CSDLPT]
GO
/****** Object:  StoredProcedure [dbo].[SP_Answer_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Answer_Add]
(
	@BAITHI INT,
	@STT INT,
	@CAUHOI INT,
	@DACHON NCHAR(1)
) AS
BEGIN
    INSERT INTO dbo.BAITHI
    (
        BAITHI,
        STT,
        CAUHOI,
        DACHON
    )
    VALUES
    (   @BAITHI,   -- BAITHI - int
        @STT,   -- STT - int
        @CAUHOI,   -- CAUHOI - int
        @DACHON
        )
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Answer_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Answer_Delete]
(
	@BAITHI INT,
	@STT INT
) AS
BEGIN
    DELETE FROM dbo.BAITHI WHERE BAITHI = @BAITHI AND STT = @STT
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Answer_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
	SP_Answer_

	GetAll
	GetById
	Add
	Update
	Delete
*/

CREATE PROC [dbo].[SP_Answer_GetAll] AS
BEGIN
    SELECT * FROM dbo.BAITHI
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Answer_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Answer_GetById]
(
	@BAITHI INT,
	@STT INT
) AS
BEGIN
    SELECT * FROM dbo.BAITHI
	WHERE BAITHI = @BAITHI AND
	STT = @STT
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Answer_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Answer_Update]
(
	@BAITHI INT,
	@STT INT,
	@CAUHOI INT,
	@DACHON NCHAR(1)
) AS
BEGIN
    UPDATE dbo.BAITHI
	SET CAUHOI = @CAUHOI,
		DACHON = @DACHON
	WHERE
		BAITHI = @BAITHI AND
		STT = @STT
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Branch_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Branch_Add]
(
    @MACS NCHAR(3),
    @TENCS NVARCHAR(50),
    @DIACHI NVARCHAR(100)
)
AS
BEGIN
    INSERT INTO dbo.COSO
    (
        MACS,
        TENCS,
        DIACHI
    )
    VALUES
    (   @MACS,  -- MACS - nchar(3)
        @TENCS, -- TENCS - nvarchar(50)
        @DIACHI -- DIACHI - nvarchar(100)
        );
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Branch_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Branch_Delete]
(@MACS NCHAR(3))
AS
BEGIN
    DELETE FROM dbo.COSO
    WHERE MACS = @MACS;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Branch_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*

SP_Branch_

GetAll
GetById
Add
Update
Delete

*/

CREATE PROC [dbo].[SP_Branch_GetAll]
AS
BEGIN
    SELECT *
    FROM dbo.COSO;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Branch_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Branch_GetById]
(@MACS NCHAR(3))
AS
BEGIN
    SELECT *
    FROM dbo.COSO
    WHERE MACS = @MACS;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Branch_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Branch_Update]
(
    @MACS NCHAR(3),
    @TENCS NVARCHAR(50),
    @DIACHI NVARCHAR(100)
)
AS
BEGIN
    UPDATE dbo.COSO
    SET TENCS = @TENCS,
        DIACHI = @DIACHI
    WHERE MACS = @MACS;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Classroom_Add]
(
    @MALOP NCHAR(8),
	@TENLOP NVARCHAR(40),
	@MAKH NCHAR(8)
)
AS
BEGIN
    INSERT INTO dbo.LOP
    (
        MALOP,
        TENLOP,
        MAKH
    )
    VALUES
    (   @MALOP, -- MALOP - nchar(8)
        @TENLOP, -- TENLOP - nvarchar(40)
        @MAKH
        )
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_Check]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Classroom_Check]
(@MALOP NCHAR(8))
AS
BEGIN
    IF EXISTS (SELECT MALOP FROM dbo.LOP WHERE MALOP = @MALOP)
        RETURN 1;
    IF EXISTS
    (
        SELECT MALOP
        FROM LINK2.TN_CSDLPT.dbo.LOP
       WHERE MALOP = @MALOP
    )
        RETURN 1;
	RETURN 0;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Classroom_Delete]
(@MALOP NCHAR(8))
AS
BEGIN
    DELETE FROM dbo.LOP
    WHERE MALOP = @MALOP;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*

SP_Classroom_

GetAll
GetById
Add
Update
Delete

*/

CREATE PROC [dbo].[SP_Classroom_GetAll]
AS
BEGIN
    SELECT *
    FROM dbo.LOP;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Classroom_GetById]
(@MALOP NCHAR(8))
AS
BEGIN
    SELECT *
    FROM dbo.LOP
    WHERE MALOP = @MALOP;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Classroom_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Classroom_Update]
(
    @MALOP NCHAR(8),
	@TENLOP NVARCHAR(40),
	@MAKH NCHAR(8)
)
AS
BEGIN
    UPDATE dbo.LOP
    SET TENLOP = @TENLOP,
		MAKH = @MAKH
    WHERE MALOP = @MALOP;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Department_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Department_Add]
(
    @MAKH NCHAR(8),
    @TENKH NVARCHAR(40),
    @MACS NCHAR(3)
)
AS
BEGIN
    INSERT INTO dbo.KHOA
    (
        MAKH,
        TENKH,
        MACS
    )
    VALUES
    (   @MAKH, -- MAKH - nchar(8)
        @TENKH, -- TENKH - nvarchar(40)
        @MACS -- MACS - nchar(3)
        )
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Department_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Department_Delete]
(@MAKH NCHAR(8))
AS
BEGIN
    DELETE FROM dbo.KHOA
    WHERE MAKH = @MAKH;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Department_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*

SP_Department_

GetAll
GetById
Add
Update
Delete

*/

CREATE PROC [dbo].[SP_Department_GetAll]
AS
BEGIN
    SELECT *
    FROM dbo.KHOA;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Department_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Department_GetById]
(@MAKH NCHAR(8))
AS
BEGIN
    SELECT *
    FROM dbo.KHOA
    WHERE MAKH = @MAKH;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Department_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Department_Update]
(
    @MAKH NCHAR(8),
    @TENKH NVARCHAR(40),
    @MACS NCHAR(3)
)
AS
BEGIN
    UPDATE dbo.KHOA
    SET TENKH = @TENKH,
		MACS = @MACS
    WHERE MAKH = @MAKH;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Question_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Question_Add]
(
    @CAUHOI INT,
    @MAMH NCHAR(5),
    @TRINHDO CHAR(1),
    @NOIDUNG NTEXT,
    @A NTEXT,
    @B NTEXT,
    @C NTEXT,
    @D NTEXT,
    @DAP_AN NCHAR(1),
    @MAGV NCHAR(8)
)
AS
BEGIN
    INSERT INTO dbo.BODE
    (
        CAUHOI,
        MAMH,
        TRINHDO,
        NOIDUNG,
        A,
        B,
        C,
        D,
        DAP_AN,
        MAGV
    )
    VALUES
    (   @CAUHOI,  -- CAUHOI - int
        @MAMH,    -- MAMH - nchar(5)
        @TRINHDO, -- TRINHDO - char(1)
        @NOIDUNG, -- NOIDUNG - ntext
        @A,       -- A - ntext
        @B,       -- B - ntext
        @C,       -- C - ntext
        @D,       -- D - ntext
        @DAP_AN,  -- DAP_AN - nchar(1)
        @MAGV     -- MAGV - nchar(8)
        );
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Question_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Question_Delete]
(
	@CAUHOI INT
) AS
BEGIN
    DELETE FROM dbo.BODE WHERE CAUHOI = @CAUHOI
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Question_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*

SP_Question_

GetAll
GetById
Add
Update
Delete

*/

CREATE PROC [dbo].[SP_Question_GetAll]
AS
BEGIN
    SELECT *
    FROM dbo.BODE;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Question_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Question_GetById]
(@CAUHOI INT)
AS
BEGIN
    SELECT *
    FROM dbo.BODE
    WHERE CAUHOI = @CAUHOI;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Question_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Question_Update]
(
    @CAUHOI INT,
    @MAMH NCHAR(5),
    @TRINHDO CHAR(1),
    @NOIDUNG NTEXT,
    @A NTEXT,
    @B NTEXT,
    @C NTEXT,
    @D NTEXT,
    @DAP_AN NCHAR(1),
    @MAGV NCHAR(8)
)
AS
BEGIN
    UPDATE dbo.BODE
    SET MAMH = @MAMH,
        TRINHDO = @TRINHDO,
        NOIDUNG = @NOIDUNG,
        A = @A,
        B = @B,
        C = @C,
        D = @D,
        DAP_AN = @DAP_AN,
        MAGV = @MAGV
    WHERE CAUHOI = @CAUHOI;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Register_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Register_Add]
(
	@MAGV NCHAR(8),
	@MAMH NCHAR(5),
	@MALOP NCHAR(8),
	@TRINHDO NCHAR(1),
	@NGAYTHI DATETIME,
	@LAN SMALLINT,
	@SOCAUTHI SMALLINT,
	@THOIGIAN SMALLINT
) AS
BEGIN
	INSERT INTO dbo.GIAOVIEN_DANGKY
	(
	    MAGV,
	    MAMH,
	    MALOP,
	    TRINHDO,
	    NGAYTHI,
	    LAN,
	    SOCAUTHI,
	    THOIGIAN
	)
	VALUES
	(   @MAGV,       -- MAGV - nchar(8)
	    @MAMH,       -- MAMH - nchar(5)
	    @MALOP,       -- MALOP - nchar(8)
	    @TRINHDO,       -- TRINHDO - nchar(1)
	    @NGAYTHI, -- NGAYTHI - datetime
	    @LAN,         -- LAN - smallint
	    @SOCAUTHI,         -- SOCAUTHI - smallint
	    @THOIGIAN         -- THOIGIAN - smallint
	    )
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Register_Check]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Register_Check] (
	@MAMH NCHAR(5),
	@MALOP NCHAR(8),
	@LAN SMALLINT
) AS BEGIN
	IF EXISTS (SELECT * FROM dbo.GIAOVIEN_DANGKY WHERE MAMH = @MAMH AND MALOP = @MALOP AND LAN = @LAN) RETURN 1
	ELSE RETURN 0
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Register_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Register_Delete]
(
	@MAMH NCHAR(5),
	@MALOP NCHAR(8),
	@LAN SMALLINT
) AS
BEGIN
	DELETE FROM dbo.GIAOVIEN_DANGKY WHERE
		MAMH = @MAMH AND
		MALOP = @MALOP AND
		LAN = @LAN
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Register_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Register_GetAll] AS
BEGIN
	SELECT * FROM dbo.GIAOVIEN_DANGKY
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Register_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Register_GetById]
(
	@MAMH NCHAR(5),
	@MALOP NCHAR(8),
	@LAN SMALLINT
) AS
BEGIN
	SELECT * FROM dbo.GIAOVIEN_DANGKY WHERE MAMH = @MAMH AND
	MALOP = @MALOP AND LAN = @LAN
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Register_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Register_Update]
(
	@MAGV NCHAR(8),
	@MAMH NCHAR(5),
	@MALOP NCHAR(8),
	@TRINHDO NCHAR(1),
	@NGAYTHI DATETIME,
	@LAN SMALLINT,
	@SOCAUTHI SMALLINT,
	@THOIGIAN SMALLINT
) AS
BEGIN
	UPDATE dbo.GIAOVIEN_DANGKY
	SET MAGV = @MAGV,
		TRINHDO = @TRINHDO,
		NGAYTHI = @NGAYTHI,
		SOCAUTHI = @SOCAUTHI,
		THOIGIAN = @THOIGIAN
	WHERE MAMH = @MAMH AND
		MALOP = @MALOP AND
		LAN = @LAN
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Student_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Student_Add]
(
	@MASV NCHAR(8),
	@HO NVARCHAR(10),
	@TEN NVARCHAR(10),
	@NGAYSINH DATETIME,
	@DIACHI NVARCHAR(40),
	@MALOP NCHAR(8),
	@MATKHAU VARCHAR(20)
) AS
BEGIN
    INSERT INTO dbo.SINHVIEN
    (
        MASV,
        HO,
        TEN,
        NGAYSINH,
        DIACHI,
        MALOP,
        MATKHAU
    )
    VALUES
    (   @MASV,       -- MASV - nchar(8)
        @HO,       -- HO - nvarchar(40)
        @TEN,       -- TEN - nvarchar(10)
        @NGAYSINH, -- NGAYSINH - datetime
        @DIACHI,       -- DIACHI - nvarchar(40)
        @MALOP,       -- MALOP - nchar(8)
        @MATKHAU      -- rowguid - uniqueidentifier
        )
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Student_Check]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Student_Check]
(@MASV NCHAR(8))
AS
BEGIN
    IF EXISTS (SELECT MASV FROM dbo.SINHVIEN WHERE MASV = @MASV)
        RETURN 1;
    IF EXISTS
    (
        SELECT MASV
        FROM LINK2.TN_CSDLPT.dbo.SINHVIEN
        WHERE MASV = @MASV
    )
        RETURN 1;
	RETURN 0;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Student_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Student_Delete]
(@MASV NCHAR(8))
AS
BEGIN
    DELETE
    FROM dbo.SINHVIEN
    WHERE MASV = @MASV;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Student_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Student_GetAll] AS
BEGIN
    SELECT * FROM dbo.SINHVIEN
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Student_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Student_GetById] (@MASV NCHAR(8)) AS
BEGIN
    SELECT * FROM dbo.SINHVIEN WHERE MASV = @MASV
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Student_GetFromClass]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
	SP_Teacher_
	
	GetFromClass
	
	GetAll
	GetById
	Add
	Update
	Delete
*/
CREATE PROC [dbo].[SP_Student_GetFromClass]
(@MALOP NCHAR(8))
AS
BEGIN
    SELECT * FROM dbo.SINHVIEN WHERE MALOP = @MALOP
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Student_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Student_Update]
(
    @MASV NCHAR(8),
    @HO NVARCHAR(10),
    @TEN NVARCHAR(10),
    @NGAYSINH DATETIME,
    @DIACHI NVARCHAR(40),
    @MALOP NCHAR(8),
    @MATKHAU VARCHAR(20)
)
AS
BEGIN
    UPDATE dbo.SINHVIEN
    SET HO = @HO,
        TEN = @TEN,
        NGAYSINH = @NGAYSINH,
        DIACHI = @DIACHI,
        MALOP = @MALOP,
        MATKHAU = @MATKHAU
    WHERE MASV = @MASV;
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Subject_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Subject_Add]
(
    @MAMH NCHAR(5),
    @TENMH NVARCHAR(40)
)
AS
BEGIN
    INSERT INTO dbo.MONHOC
    (
        MAMH,
        TENMH
    )
    VALUES
    (   @MAMH, -- MAMH - nchar(5)
        @TENMH -- TENMH - nvarchar(40)
        );
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Subject_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Subject_Delete] (@MAMH NCHAR(5)) AS
BEGIN
    DELETE FROM dbo.MONHOC WHERE MAMH = @MAMH
END

GO
/****** Object:  StoredProcedure [dbo].[SP_Subject_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Subject_GetAll] AS BEGIN
	SELECT * FROM dbo.MONHOC
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Subject_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Subject_GetById] (@MAMH NCHAR(5)) AS
BEGIN
    SELECT * FROM dbo.MONHOC WHERE MAMH = @MAMH
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Subject_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Subject_Update]
(
    @MAMH NCHAR(5),
    @TENMH NVARCHAR(40)
)
AS
BEGIN
    UPDATE dbo.MONHOC SET TENMH = @TENMH WHERE MAMH=@MAMH
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Tao_Login]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROC [dbo].[SP_Tao_Login]
  @LGNAME VARCHAR(50),
  @PASS VARCHAR(50),
  @USERNAME VARCHAR(50),
  @ROLE VARCHAR(50)
AS
  DECLARE @RET INT
  EXEC @RET= SP_ADDLOGIN @LGNAME, @PASS,'TN_CSDLPT'
  IF (@RET =1)  -- LOGIN NAME BI TRUNG
     RETURN 1
 
  EXEC @RET= SP_GRANTDBACCESS @LGNAME, @USERNAME
  IF (@RET =1)  -- USER  NAME BI TRUNG
  BEGIN
       EXEC SP_DROPLOGIN @LGNAME
       RETURN 2
  END
  EXEC sp_addrolemember @ROLE, @USERNAME
  IF @ROLE= 'TRUONG' OR @ROLE= 'COSO'
  BEGIN 
    EXEC sp_addsrvrolemember @LGNAME, 'SecurityAdmin'
    EXEC sp_addsrvrolemember @LGNAME, 'ProcessAdmin'
  END
RETURN 0  -- THANH CONG
GO

GO
/****** Object:  StoredProcedure [dbo].[SP_TaoDeThi]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_TaoDeThi]
(
    @N INT,
    @MAMH NCHAR(5),
    @TDC CHAR(1)
)
AS
BEGIN

    --SET NOCOUNT ON
    SET NOCOUNT ON;

    --KHỞI TẠO CÁC GIÁ TRỊ VÀ TẠO BẢNG TẠM

    DECLARE @NT INT = FLOOR(@N * 0.3);

    DECLARE @TDT CHAR(1);

    IF @TDC = 'A'
        SET @TDT = 'B';
    ELSE IF @TDC = 'B'
        SET @TDT = 'C';
    ELSE
        SET @TDT = 'D';

    CREATE TABLE #TEMP
    (
        CAUHOI INT,
        MAMH NCHAR(5),
        TRINHDO CHAR(1),
        NOIDUNG NTEXT,
        A NTEXT,
        B NTEXT,
        C NTEXT,
        D NTEXT,
        DAP_AN NCHAR(1),
        MAGV CHAR(8)
    );

    --XỬ LÝ
    DECLARE @RET INT,
            @DEL INT;

    --THÊM B
    INSERT INTO #TEMP
    (
        CAUHOI,
        MAMH,
        TRINHDO,
        NOIDUNG,
        A,
        B,
        C,
        D,
        DAP_AN,
        MAGV
    )
    SELECT TOP (@NT)
           CAUHOI,
           MAMH,
           TRINHDO,
           NOIDUNG,
           A,
           B,
           C,
           D,
           DAP_AN,
           MAGV
    FROM dbo.BODE
    WHERE MAGV IN
          (
              SELECT MAGV FROM dbo.GIAOVIEN WHERE MAKH IN ( SELECT MAKH FROM dbo.KHOA )
          )
          AND MAMH = @MAMH
          AND TRINHDO = @TDT
    ORDER BY NEWID();

    SET @RET = @@ROWCOUNT;

    --SET GIÁ TRỊ @DEL
    SET @DEL = @NT - @RET;

    --THÊM A
    INSERT INTO #TEMP
    (
        CAUHOI,
        MAMH,
        TRINHDO,
        NOIDUNG,
        A,
        B,
        C,
        D,
        DAP_AN,
        MAGV
    )
    SELECT TOP (@N - @RET)
           CAUHOI,
           MAMH,
           TRINHDO,
           NOIDUNG,
           A,
           B,
           C,
           D,
           DAP_AN,
           MAGV
    FROM dbo.BODE
    WHERE MAGV IN
          (
              SELECT MAGV FROM dbo.GIAOVIEN WHERE MAKH IN ( SELECT MAKH FROM dbo.KHOA )
          )
          AND MAMH = @MAMH
          AND TRINHDO = @TDC
    ORDER BY NEWID();

    --KIỂM TRA TRẢ VỀ
    IF (@@ROWCOUNT + @NT - @DEL = @N)
    BEGIN
        SELECT *
        FROM #TEMP
        ORDER BY NEWID();
        RETURN;
    END;

    --GÁN LẠI @DEL
    SELECT @RET = COUNT(CAUHOI)
    FROM #TEMP;

    IF (@N - @RET < @DEL)
        SET @DEL = @N - @RET;

    --THÊM B2
    INSERT INTO #TEMP
    (
        CAUHOI,
        MAMH,
        TRINHDO,
        NOIDUNG,
        A,
        B,
        C,
        D,
        DAP_AN,
        MAGV
    )
    SELECT TOP (@DEL)
           CAUHOI,
           MAMH,
           TRINHDO,
           NOIDUNG,
           A,
           B,
           C,
           D,
           DAP_AN,
           MAGV
    FROM dbo.BODE
    WHERE MAGV NOT IN
          (
              SELECT MAGV FROM dbo.GIAOVIEN WHERE MAKH IN ( SELECT MAKH FROM dbo.KHOA )
          )
          AND MAMH = @MAMH
          AND TRINHDO = @TDT
    ORDER BY NEWID();

    SELECT @RET = COUNT(CAUHOI)
    FROM #TEMP;

    --THÊM A2
    INSERT INTO #TEMP
    (
        CAUHOI,
        MAMH,
        TRINHDO,
        NOIDUNG,
        A,
        B,
        C,
        D,
        DAP_AN,
        MAGV
    )
    SELECT TOP (@N - @RET)
           CAUHOI,
           MAMH,
           TRINHDO,
           NOIDUNG,
           A,
           B,
           C,
           D,
           DAP_AN,
           MAGV
    FROM dbo.BODE
    WHERE MAGV NOT IN
          (
              SELECT MAGV FROM dbo.GIAOVIEN WHERE MAKH IN ( SELECT MAKH FROM dbo.KHOA )
          )
          AND MAMH = @MAMH
          AND TRINHDO = @TDC
    ORDER BY NEWID();

    SELECT *
    FROM #TEMP
    ORDER BY NEWID();

END;



GO
/****** Object:  StoredProcedure [dbo].[SP_Teacher_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Teacher_Add]
(
    @MAGV NCHAR(8),
    @HO NVARCHAR(40),
    @TEN NVARCHAR(40),
    @HOCVI NVARCHAR(40),
    @MAKHOA NCHAR(8)
)
AS
BEGIN
    INSERT INTO dbo.GIAOVIEN
    (
        MAGV,
        HO,
        TEN,
        HOCVI,
        MAKH
    )
    VALUES
    (   @MAGV, -- MAGV - nchar(8)
        @HO, -- HO - nvarchar(40)
        @TEN, -- TEN - nvarchar(40)
        @HOCVI, -- HOCVI - nvarchar(40)
        @MAKHOA
        )
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Teacher_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Teacher_Delete]
(@MAGV NCHAR(8))
AS
BEGIN
    DELETE FROM dbo.GIAOVIEN
    WHERE MAGV = @MAGV;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Teacher_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Teacher_GetAll]

AS
BEGIN
    SELECT * FROM dbo.GIAOVIEN
END;


GO
/****** Object:  StoredProcedure [dbo].[SP_Teacher_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Teacher_GetById] (@MAGV NCHAR(8)) AS
BEGIN
    SELECT * FROM dbo.GIAOVIEN WHERE MAGV = @MAGV
END


GO
/****** Object:  StoredProcedure [dbo].[SP_Teacher_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Teacher_Update]
(
    @MAGV NCHAR(8),
    @HO NVARCHAR(40),
    @TEN NVARCHAR(40),
    @HOCVI NVARCHAR(40),
    @MAKH NCHAR(8)
)
AS
BEGIN
    UPDATE dbo.GIAOVIEN
    SET HO = @HO,
        TEN = @TEN,
        HOCVI = @HOCVI,
        MAKH = @MAKH
    WHERE MAGV = @MAGV;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_ThemBoDeTest]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_ThemBoDeTest]
(
    @B INT,
    @A INT,
    @B2 INT,
    @A2 INT,
    @TDC CHAR(1),
    @MAMH NCHAR(5),
    @MAGV NCHAR(8)
)
AS
BEGIN
    DECLARE @TDT CHAR(1);
    IF @TDC = 'A'
        SET @TDT = 'B';
    ELSE IF @TDC = 'B'
        SET @TDT = 'C';
    ELSE
        SET @TDT = 'D';
    DECLARE @MAGV2 NCHAR(8);
    IF @MAGV = 'TH123   '
        SET @MAGV2 = N'TH234   ';
    ELSE
        SET @MAGV2 = N'TH123   ';
    --
    DECLARE @RET INT,
            @I INT;
    --THÊM B1
    SELECT @RET = COUNT(CAUHOI)
    FROM dbo.BODE;
    SET @I = 0;
    WHILE @I < @B
    BEGIN
        SET @I = @I + 1;
        INSERT INTO dbo.BODE
        (
            CAUHOI,
            MAMH,
            TRINHDO,
            NOIDUNG,
            A,
            B,
            C,
            D,
            DAP_AN,
            MAGV
        )
        VALUES
        (@RET + @I, @MAMH, @TDT, N'Câu hỏi:', N'A', N'B', N'C', N'D', N'D', @MAGV);
    END;
	--THÊM A1
    SELECT @RET = COUNT(CAUHOI)
    FROM dbo.BODE;
    SET @I = 0;
    WHILE @I < @A
    BEGIN
        SET @I = @I + 1;
        INSERT INTO dbo.BODE
        (
            CAUHOI,
            MAMH,
            TRINHDO,
            NOIDUNG,
            A,
            B,
            C,
            D,
            DAP_AN,
            MAGV
        )
        VALUES
        (@RET + @I, @MAMH, @TDC, N'Câu hỏi:', N'A', N'B', N'C', N'D', N'D', @MAGV);
    END;
    --THÊM B2
    SELECT @RET = COUNT(CAUHOI)
    FROM dbo.BODE;
    SET @I = 0;
    WHILE @I < @B2
    BEGIN
        SET @I = @I + 1;
        INSERT INTO dbo.BODE
        (
            CAUHOI,
            MAMH,
            TRINHDO,
            NOIDUNG,
            A,
            B,
            C,
            D,
            DAP_AN,
            MAGV
        )
        VALUES
        (@RET + @I, @MAMH, @TDT, N'Câu hỏi:', N'A', N'B', N'C', N'D', N'D', @MAGV2);
    END;
	--THÊM A2
    SELECT @RET = COUNT(CAUHOI)
    FROM dbo.BODE;
    SET @I = 0;
    WHILE @I < @A2
    BEGIN
        SET @I = @I + 1;
        INSERT INTO dbo.BODE
        (
            CAUHOI,
            MAMH,
            TRINHDO,
            NOIDUNG,
            A,
            B,
            C,
            D,
            DAP_AN,
            MAGV
        )
        VALUES
        (@RET + @I, @MAMH, @TDC, N'Câu hỏi:', N'A', N'B', N'C', N'D', N'D', @MAGV2);
    END;

END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Transcript_Add]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Transcript_Add]
(
    @MASV NCHAR(8),
    @MAMH NCHAR(5),
    @LAN SMALLINT,
    @NGAYTHI DATETIME,
    @DIEM FLOAT
)
AS
BEGIN
    INSERT INTO dbo.BANGDIEM
    (
        MASV,
        MAMH,
        LAN,
        NGAYTHI,
        DIEM
    )
    VALUES
    (   @MASV,    -- MASV - nchar(8)
        @MAMH,    -- MAMH - nchar(5)
        @LAN,     -- LAN - smallint
        @NGAYTHI, -- NGAYTHI - datetime
        @DIEM);
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Transcript_Delete]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Transcript_Delete]
(
    @MASV NCHAR(8),
    @MAMH NCHAR(5),
    @LAN SMALLINT
)
AS
BEGIN
    DELETE FROM dbo.BANGDIEM
    WHERE MASV = @MASV
          AND MAMH = @MAMH
          AND LAN = @LAN;
END;
GO
/****** Object:  StoredProcedure [dbo].[SP_Transcript_GetAll]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
	SP_Transcipt_

	GetAll
	GetById
	Add
	Delete
	Update
*/

CREATE PROC [dbo].[SP_Transcript_GetAll]
AS
BEGIN
    SELECT *
    FROM dbo.BANGDIEM;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Transcript_GetById]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Transcript_GetById]
(
    @MASV NCHAR(8),
    @MAMH NCHAR(5),
    @LAN SMALLINT
)
AS
BEGIN
    SELECT *
    FROM dbo.BANGDIEM
    WHERE MASV = @MASV
          AND MAMH = @MAMH
          AND LAN = @LAN;
END;

GO
/****** Object:  StoredProcedure [dbo].[SP_Transcript_Update]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Transcript_Update]
(
    @MASV NCHAR(8),
    @MAMH NCHAR(5),
    @LAN SMALLINT,
    @NGAYTHI DATETIME,
    @DIEM FLOAT
)
AS
BEGIN
    UPDATE dbo.BANGDIEM
    SET NGAYTHI = @NGAYTHI,
        DIEM = @DIEM
    WHERE MASV = @MASV
          AND MAMH = @MAMH
          AND LAN = @LAN;
END;
GO
/****** Object:  StoredProcedure [dbo].[SP_Xoa_Login]    Script Date: 10/21/2021 23:07:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[SP_Xoa_Login]
  @LGNAME VARCHAR(50),
  @USRNAME VARCHAR(50)
  
AS
  EXEC SP_DROPUSER @USRNAME
  EXEC SP_DROPLOGIN @LGNAME



GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Xem_Bang_Diem_Lop]    Script Date: 11/12/2021 11:11:09 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROC [dbo].[SP_Xem_Bang_Diem_Lop]
@MAMONHOC NVARCHAR (5),
@MALOPHOC NVARCHAR (8),
@LANTHI SMALLINT
AS
SELECT SV.MASV, HO, TEN, DIEM
FROM (SELECT MASV, HO, TEN FROM SINHVIEN
		WHERE MALOP = @MALOPHOC) SV,
	(SELECT MASV, DIEM FROM BANGDIEM WHERE MAMH = @MAMONHOC AND LAN = @LANTHI) BD
WHERE SV.MASV = BD.MASV
ORDER BY TEN
GO


USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Xem_Bang_Diem_Sinh_Vien]    Script Date: 11/12/2021 11:11:14 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROC [dbo].[SP_Xem_Bang_Diem_Sinh_Vien]
@MSSV NVARCHAR(8),
@MAMONHOC NVARCHAR(5),
@LANTHI SMALLINT
AS
IF @MSSV IN (SELECT MASV FROM SINHVIEN)
BEGIN
	DECLARE @TENLOP NVARCHAR(40), @TENMH NVARCHAR(40), @HOTEN NVARCHAR(80)
	SELECT @TENLOP = TENLOP FROM LOP, SINHVIEN WHERE SINHVIEN.MASV = @MSSV AND SINHVIEN.MALOP = LOP.MALOP
	SELECT @TENMH = TENMH FROM MONHOC WHERE MAMH = @MAMONHOC
	SELECT @HOTEN = HO + ' ' + TEN FROM SINHVIEN WHERE MASV = @MSSV
	SELECT MASV,HOTEN = @HOTEN, LOP=@TENLOP, TENMH=@TENMH,LAN,NGAYTHI,BAITHI,DIEM
	FROM BANGDIEM WHERE MASV = @MSSV AND MAMH = @MAMONHOC AND LAN = @LANTHI
END
ELSE
BEGIN
	RAISERROR('Mã sinh viên không tồn tại ở cơ sở hiện tại',16,1)
END
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Xem_Danh_Sach_Dang_Ky]    Script Date: 11/12/2021 11:11:29 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROC [dbo].[SP_Xem_Danh_Sach_Dang_Ky]
@FROMDATE DATETIME,
@TODATE DATETIME
AS
AS
SELECT GVDK.MALOP, TENLOP, GVDK.MAMH, TENMH, TENGV, SOCAUTHI, NGAYTHI, GVDK.LAN, KHOA.MACS INTO #DSDK
FROM (SELECT MAGV, TENGV = HO + ' ' + TEN FROM GIAOVIEN) GV,
	 (SELECT MAGV, MAMH, MALOP, NGAYTHI, SOCAUTHI, LAN FROM GIAOVIEN_DANGKY 
		WHERE NGAYTHI BETWEEN @FROMDATE AND @TODATE) GVDK, 
	    KHOA, MONHOC, LOP
WHERE LOP.MALOP = GVDK.MALOP
AND MONHOC.MAMH = GVDK.MAMH 
AND GV.MAGV = GVDK.MAGV
AND LOP.MAKH = KHOA.MAKH

SELECT GIAOVIEN_DANGKY.MALOP, BANGDIEM.MAMH, BANGDIEM.LAN INTO #CHECK
FROM BANGDIEM, GIAOVIEN_DANGKY, SINHVIEN
WHERE BANGDIEM.MASV = SINHVIEN.MASV 
AND SINHVIEN.MALOP = GIAOVIEN_DANGKY.MALOP
AND BANGDIEM.LAN = GIAOVIEN_DANGKY.LAN
AND BANGDIEM.MAMH = GIAOVIEN_DANGKY.MAMH

SELECT TENLOP, TENMH, TENGV, SOCAUTHI, NGAYTHI,
		CASE WHEN EXISTS(SELECT * FROM #CHECK WHERE #CHECK.MALOP = #DSDK.MALOP AND #CHECK.MAMH = #DSDK.MAMH AND #CHECK.LAN = #DSDK.LAN)
			THEN 'X' ELSE ''
		END AS DATHI, MACS
FROM #DSDK
ORDER BY NGAYTHI
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Xem_Ket_Qua]    Script Date: 11/12/2021 11:11:41 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE proc [dbo].[SP_Xem_Ket_Qua]
@BAITHI INT
AS
SELECT STT,CAUHOI,DACHON
FROM BAITHI WHERE BAITHI.BAITHI = @BAITHI
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Get_Register_List]    Script Date: 11/12/2021 11:12:26 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Get_Register_List]
@FROMDATE DATETIME,
@TODATE DATETIME
AS
SET NOCOUNT ON
IF (1=0) SET FMTONLY OFF
SET XACT_ABORT ON
CREATE TABLE #TMP(
	TENLOP NVARCHAR(40), 
	TENMH NVARCHAR(40), 
	TENGV NVARCHAR(100), 
	SOCAUTHI SMALLINT, 
	NGAYTHI DATETIME,
	DATHI NCHAR(1), 
	MACS NCHAR(3)
)
BEGIN DISTRIBUTED TRANSACTION;
	INSERT INTO #TMP EXEC SP_Xem_Danh_Sach_Dang_Ky @FROMDATE, @TODATE
	INSERT INTO #TMP EXEC LINK1.TN_CSDLPT.dbo.SP_Xem_Danh_Sach_Dang_Ky @FROMDATE, @TODATE
COMMIT TRANSACTION;
SELECT * FROM #TMP
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Lay_Thong_Tin_Dang_Nhap]    Script Date: 11/12/2021 11:12:55 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROC [dbo].[SP_Lay_Thong_Tin_Dang_Nhap]
@TENLOGIN NVARCHAR (50)
AS
DECLARE @TENUSER NVARCHAR(80)
SELECT @TENUSER=NAME FROM sys.sysusers WHERE sid = SUSER_SID(@TENLOGIN)
 
 SELECT USERNAME = @TENUSER, 
  HOTEN = (SELECT HO+ ' '+ TEN FROM GIAOVIEN  WHERE MAGV = @TENUSER ),
   TENNHOM= NAME
   FROM sys.sysusers 
   WHERE UID = (SELECT GROUPUID 
                 FROM SYS.SYSMEMBERS 
                   WHERE MEMBERUID= (SELECT UID FROM sys.sysusers 
                                      WHERE NAME=@TENUSER))
GO

USE [TN_CSDLPT]
GO

/****** Object:  View [dbo].[Get_Subcribers]    Script Date: 11/17/2021 8:15:36 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[Get_Subcribers]
AS
SELECT  TENCN=PUBS.description, TENSERVER= subscriber_server
   FROM dbo.sysmergepublications PUBS,  dbo.sysmergesubscriptions SUBS
   WHERE PUBS.pubid= SUBS.PUBID  AND PUBS.publisher <> SUBS.subscriber_server AND PUBS.name != 'TN_CSDLPT_TRACUU'
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Check_Teacher_In_Branch]    Script Date: 12/4/2021 12:06:53 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

create proc [dbo].[SP_Check_Teacher_In_Branch]
@MAGV nchar(8)
AS
DECLARE @MAKH nchar(8)
SELECT @MAKH = MAKH FROM GIAOVIEN WHERE MAGV = @MAGV
IF EXISTS (SELECT MAKH FROM KHOA WHERE MAKH = @MAKH)
	RETURN 1
ELSE
	RETURN 0
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Check_Student_Login]    Script Date: 12/4/2021 4:17:23 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Check_Student_Login]
@MASV nchar(8),
@MATKHAU varchar(20)
AS
IF @MASV IN (SELECT MASV FROM SINHVIEN)
	BEGIN
		SELECT MASV, HO, TEN FROM SINHVIEN WHERE MATKHAU = @MATKHAU AND MASV = @MASV
	END
ELSE
	BEGIN
		RAISERROR('Mã sinh viên không tồn tại ở cơ sở hiện tại !',16,1)
	END
GO

USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Get_Register_For_Student]    Script Date: 12/10/2021 9:58:03 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Get_Register_For_Student]
@MASV nchar(8)
AS
DECLARE @MALOP nchar(8), @RET INT
SELECT @MALOP = MALOP FROM SINHVIEN WHERE MASV = @MASV

SELECT GIAOVIEN_DANGKY.MAMH, TENMH, NGAYTHI, LAN, SOCAUTHI, THOIGIAN, TRINHDO
FROM GIAOVIEN_DANGKY, MONHOC
WHERE GIAOVIEN_DANGKY.MALOP = @MALOP AND GIAOVIEN_DANGKY.MAMH = MONHOC.MAMH
ORDER BY NGAYTHI
GO


USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Is_Take_Exam]    Script Date: 12/10/2021 9:58:38 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Is_Take_Exam]
@MASV nchar(8),
@MAMH nchar(5),
@LAN smallint
AS
IF EXISTS (SELECT * FROM BANGDIEM WHERE MASV = @MASV AND MAMH = @MAMH AND LAN = @LAN)
	RETURN 1
ELSE
	RETURN 0
GO


USE [TN_CSDLPT]
GO

/****** Object:  StoredProcedure [dbo].[SP_Department_Check]    Script Date: 12/11/2021 8:33:27 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROC [dbo].[SP_Department_Check]
(@MAKHOA NCHAR(8))
AS
BEGIN
    IF EXISTS (SELECT MAKH FROM KHOA WHERE MAKH= @MAKHOA)
        RETURN 1;
    IF EXISTS
    (
        SELECT MAKH
        FROM LINK0.TN_CSDLPT.dbo.KHOA
       WHERE MAKH= @MAKHOA
    )
        RETURN 1;
	RETURN 0;
END;
GO




