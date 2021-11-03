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
 
  EXEC @RET= SP_GRANTDBACCESS
-----------------------------------------
--USE [TN_CSDLPT]



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
