import { useState, useEffect } from "react";
import { X, Loader, Save, Package } from "lucide-react";

const ArticleForm = ({ article, isOpen, onClose, onSubmit, isSubmitting }) => {
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        imageUrl: "",
        price: "",
        currency: "USD",
        availableQuantity: ""
    });

    const [errors, setErrors] = useState({});

    // Initialize form data when editing
    useEffect(() => {
        if (article) {
            setFormData({
                name: article.name || "",
                description: article.description || "",
                imageUrl: article.imageUrl || "",
                price: article.price?.toString() || "",
                currency: article.currency || "USD",
                availableQuantity: article.availableQuantity?.toString() || ""
            });
        } else {
            setFormData({
                name: "",
                description: "",
                imageUrl: "",
                price: "",
                currency: "USD",
                availableQuantity: ""
            });
        }
        setErrors({});
    }, [article, isOpen]);

    // Handle input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ""
            }));
        }
    };

    // Validate form
    const validateForm = () => {
        const newErrors = {};

        // Validate name
        if (!formData.name.trim()) {
            newErrors.name = "Product name is required";
        } else if (formData.name.trim().length > 50) {
            newErrors.name = "Product name must be 50 characters or less";
        }

        // Validate description
        if (!formData.description.trim()) {
            newErrors.description = "Description is required";
        } else if (formData.description.trim().length > 255) {
            newErrors.description = "Description must be 255 characters or less";
        }

        // Validate price
        if (!formData.price) {
            newErrors.price = "Price is required";
        } else {
            const price = parseFloat(formData.price);
            if (isNaN(price) || price <= 0) {
                newErrors.price = "Price must be a valid number greater than 0";
            } else if (price > 999999.99) {
                newErrors.price = "Price must be less than 999,999.99";
            }
        }

        // Validate currency
        if (!formData.currency) {
            newErrors.currency = "Currency is required";
        } else if (!/^[A-Z]{3}$/.test(formData.currency)) {
            newErrors.currency = "Currency must be a 3-letter code (e.g., USD, EUR)";
        }

        // Validate available quantity
        if (formData.availableQuantity === "") {
            newErrors.availableQuantity = "Available quantity is required";
        } else {
            const quantity = parseInt(formData.availableQuantity);
            if (isNaN(quantity) || quantity < 0) {
                newErrors.availableQuantity = "Available quantity must be a valid number 0 or greater";
            } else if (quantity > 999999) {
                newErrors.availableQuantity = "Available quantity must be less than 999,999";
            }
        }

        // Validate image URL (optional)
        if (formData.imageUrl && formData.imageUrl.trim()) {
            const imageUrl = formData.imageUrl.trim();
            if (imageUrl.length > 255) {
                newErrors.imageUrl = "Image URL must be 255 characters or less";
            } else if (!imageUrl.match(/^https?:\/\/.+$/) && !imageUrl.startsWith("data:image/")) {
                newErrors.imageUrl = "Image URL must be a valid HTTP(S) URL or data URL";
            }
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    // Handle form submit
    const handleSubmit = (e) => {
        e.preventDefault();
        
        if (!validateForm()) {
            return;
        }

        const submitData = {
            name: formData.name.trim(),
            description: formData.description.trim(),
            imageUrl: formData.imageUrl.trim() || null,
            price: parseFloat(formData.price),
            currency: formData.currency.toUpperCase(),
            availableQuantity: parseInt(formData.availableQuantity)
        };

        onSubmit(submitData);
    };

    // Handle close
    const handleClose = () => {
        if (!isSubmitting) {
            onClose();
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
                {/* Header */}
                <div className="flex items-center justify-between p-6 border-b border-gray-200">
                    <div className="flex items-center">
                        <Package className="h-6 w-6 text-blue-600 mr-3" />
                        <h2 className="text-xl font-bold text-gray-900">
                            {article ? "Edit Product" : "Create Product"}
                        </h2>
                    </div>
                    <button
                        onClick={handleClose}
                        disabled={isSubmitting}
                        className="text-gray-400 hover:text-gray-600 disabled:cursor-not-allowed"
                    >
                        <X className="h-6 w-6" />
                    </button>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="p-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {/* Product Name */}
                        <div className="md:col-span-2">
                            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
                                Product Name *
                            </label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.name ? 'border-red-300' : 'border-gray-300'
                                }`}
                                placeholder="Enter product name"
                                maxLength={50}
                            />
                            {errors.name && (
                                <p className="mt-1 text-sm text-red-600">{errors.name}</p>
                            )}
                        </div>

                        {/* Description */}
                        <div className="md:col-span-2">
                            <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                                Description *
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleInputChange}
                                rows={3}
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.description ? 'border-red-300' : 'border-gray-300'
                                }`}
                                placeholder="Enter product description"
                                maxLength={255}
                            />
                            <div className="flex justify-between mt-1">
                                {errors.description ? (
                                    <p className="text-sm text-red-600">{errors.description}</p>
                                ) : (
                                    <div />
                                )}
                                <p className="text-sm text-gray-500">{formData.description.length}/255</p>
                            </div>
                        </div>

                        {/* Price */}
                        <div>
                            <label htmlFor="price" className="block text-sm font-medium text-gray-700 mb-1">
                                Price *
                            </label>
                            <input
                                type="number"
                                id="price"
                                name="price"
                                value={formData.price}
                                onChange={handleInputChange}
                                step="0.01"
                                min="0"
                                max="999999.99"
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.price ? 'border-red-300' : 'border-gray-300'
                                }`}
                                placeholder="0.00"
                            />
                            {errors.price && (
                                <p className="mt-1 text-sm text-red-600">{errors.price}</p>
                            )}
                        </div>

                        {/* Currency */}
                        <div>
                            <label htmlFor="currency" className="block text-sm font-medium text-gray-700 mb-1">
                                Currency *
                            </label>
                            <select
                                id="currency"
                                name="currency"
                                value={formData.currency}
                                onChange={handleInputChange}
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.currency ? 'border-red-300' : 'border-gray-300'
                                }`}
                            >
                                <option value="USD">USD</option>
                                <option value="EUR">EUR</option>
                                <option value="GBP">GBP</option>
                                <option value="UAH">UAH</option>
                            </select>
                            {errors.currency && (
                                <p className="mt-1 text-sm text-red-600">{errors.currency}</p>
                            )}
                        </div>

                        {/* Available Quantity */}
                        <div>
                            <label htmlFor="availableQuantity" className="block text-sm font-medium text-gray-700 mb-1">
                                Available Quantity *
                            </label>
                            <input
                                type="number"
                                id="availableQuantity"
                                name="availableQuantity"
                                value={formData.availableQuantity}
                                onChange={handleInputChange}
                                min="0"
                                max="999999"
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.availableQuantity ? 'border-red-300' : 'border-gray-300'
                                }`}
                                placeholder="0"
                            />
                            {errors.availableQuantity && (
                                <p className="mt-1 text-sm text-red-600">{errors.availableQuantity}</p>
                            )}
                        </div>

                        {/* Image URL */}
                        <div className="md:col-span-2">
                            <label htmlFor="imageUrl" className="block text-sm font-medium text-gray-700 mb-1">
                                Image URL (optional)
                            </label>
                            <input
                                type="url"
                                id="imageUrl"
                                name="imageUrl"
                                value={formData.imageUrl}
                                onChange={handleInputChange}
                                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                                    errors.imageUrl ? 'border-red-300' : 'border-gray-300'
                                }`}
                                placeholder="https://example.com/image.jpg"
                                maxLength={255}
                            />
                            {errors.imageUrl && (
                                <p className="mt-1 text-sm text-red-600">{errors.imageUrl}</p>
                            )}
                            {formData.imageUrl && !errors.imageUrl && (
                                <div className="mt-2">
                                    <img
                                        src={formData.imageUrl}
                                        alt="Preview"
                                        className="h-20 w-20 object-cover rounded-md border border-gray-300"
                                        onError={(e) => {
                                            e.target.style.display = 'none';
                                        }}
                                    />
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Footer */}
                    <div className="flex items-center justify-end space-x-4 mt-8 pt-6 border-t border-gray-200">
                        <button
                            type="button"
                            onClick={handleClose}
                            disabled={isSubmitting}
                            className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                        >
                            {isSubmitting ? (
                                <Loader className="h-4 w-4 mr-2 animate-spin" />
                            ) : (
                                <Save className="h-4 w-4 mr-2" />
                            )}
                            {isSubmitting 
                                ? (article ? "Updating..." : "Creating...") 
                                : (article ? "Update Product" : "Create Product")
                            }
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ArticleForm;
