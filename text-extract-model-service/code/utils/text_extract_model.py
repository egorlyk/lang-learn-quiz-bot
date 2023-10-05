import cv2
import pytesseract

from exceptions.image_exceptions import ImageNotFoundException

RESCALED_IMAGE_DPI = 2000

def extract(image_path: str) -> str:
    """
    Extracts text content from an image using Optical Character Recognition (OCR).

    Args:
        image_path (str): The path to the input image file.

    Returns:
        str: The extracted text content from the image.

    Raises:
        ImageNotFoundException: If the specified image file does not exist or cannot be loaded.
    """

    img = cv2.imread(image_path)
    if img is None:
        raise ImageNotFoundException
    
    img = rescale_img(img, RESCALED_IMAGE_DPI)

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # Eng lang and recognize mode 4
    custom_config = r'-l eng --psm 4'

    return pytesseract.image_to_string(img, config=custom_config)

def rescale_img(img: cv2.typing.MatLike, dpi:int=300) -> cv2.typing.MatLike:
    """
    Rescale an image to specified dpi

    Args:
        img (cv2.typing.MatLike): image from cv2.imread
        dpi (int): dpi for the image rescale

    Returns:
        cv2.typing.MatLike: Rescaled image
    
    Raises:
        ImageNotFoundException: If the specified image file does not exist or cannot be loaded.
    """
    if img is None:
        raise ImageNotFoundException
    
    height, width = img.shape[:2]
    new_height = int((dpi / float(height)) * height)
    new_width = int((dpi / float(height)) * width)
    
    return cv2.resize(img, (new_width, new_height))