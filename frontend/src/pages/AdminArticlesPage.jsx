import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore.js";
import { axiosInstance } from "../lib/axios.js";
import ArticleForm from "../components/ArticleForm.jsx";
import SearchBar from "../components/SearchBar.jsx";
import Filters from "../components/Filters.jsx";
import Pagination from "../components/Pagination.jsx";
import { 
    Loader, 
    Package, 
    AlertCircle, 
    Plus, 
    Edit, 
    Trash2, 
    Eye,
    ShoppingBag 
} from "lucide-react";
import toast from "react-hot-toast";

const AdminArticlesPage = () => {
    const navigate = useNavigate();
    const { authUser } = useAuthStore();

    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Pagination state
    const [pagination, setPagination] = useState({
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        pageSize: 12
    });

    // Filter state
    const [search, setSearch] = useState("");
    const [filters, setFilters] = useState({
        minPrice: null,
        maxPrice: null,
        sortBy: "name",
        sortDir: "asc"
    });

    // Modal states
    const [showArticleForm, setShowArticleForm] = useState(false);
    const [editingArticle, setEditingArticle] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Check admin access
    useEffect(() => {
        if (!authUser?.isAdmin) {
            toast.error("Access denied. Admin only.");
            navigate("/home");
        }
    }, [authUser, navigate]);

    // Fetch articles with filters and pagination
    const fetchArticles = useCallback(async (page = 0) => {
        try {
            setLoading(true);
            setError(null);

            const params = new URLSearchParams({
                page: page.toString(),
                size: pagination.pageSize.toString(),
                sortBy: filters.sortBy,
                sortDir: filters.sortDir
            });

            if (search) params.append("search", search);
            if (filters.minPrice !== null) params.append("minPrice", filters.minPrice.toString());
            if (filters.maxPrice !== null) params.append("maxPrice", filters.maxPrice.toString());

            const response = await axiosInstance.get(`/admin/articles?${params}`);

            setArticles(response.data.content);
            setPagination({
                currentPage: response.data.number,
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                pageSize: response.data.size
            });

        } catch (error) {
            console.error("Failed to fetch articles:", error);
            if (error.response?.status === 403) {
                toast.error("Access denied. Admin only.");
                navigate("/home");
            } else {
                setError("Failed to load products. Please try again.");
                toast.error("Failed to load products");
            }
        } finally {
            setLoading(false);
        }
    }, [search, filters, pagination.pageSize, navigate]);

    // Initial load
    useEffect(() => {
        if (authUser?.isAdmin) {
            fetchArticles(0);
        }
    }, [search, filters, authUser, fetchArticles]);

    // Handle search
    const handleSearch = useCallback((searchValue) => {
        setSearch(searchValue);
    }, []);

    // Handle filters change
    const handleFiltersChange = useCallback((newFilters) => {
        setFilters(newFilters);
    }, []);

    // Handle filters clear
    const handleFiltersClear = useCallback(() => {
        setFilters({
            minPrice: null,
            maxPrice: null,
            sortBy: "name",
            sortDir: "asc"
        });
        setSearch("");
    }, []);

    // Handle page change
    const handlePageChange = useCallback((page) => {
        fetchArticles(page);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }, [fetchArticles]);

    // Handle create article
    const handleCreateArticle = () => {
        setEditingArticle(null);
        setShowArticleForm(true);
    };

    // Handle edit article
    const handleEditArticle = (article) => {
        setEditingArticle(article);
        setShowArticleForm(true);
    };

    // Handle delete article
    const handleDeleteArticle = async (articleId, articleName) => {
        if (!window.confirm(`Are you sure you want to delete "${articleName}"? This action cannot be undone.`)) {
            return;
        }

        try {
            await axiosInstance.delete(`/admin/articles/${articleId}`);
            toast.success("Product deleted successfully");
            fetchArticles(pagination.currentPage);
        } catch (error) {
            console.error("Failed to delete article:", error);
            toast.error("Failed to delete product");
        }
    };

    // Handle form submit
    const handleFormSubmit = async (articleData) => {
        setIsSubmitting(true);
        try {
            if (editingArticle) {
                // Update existing article
                await axiosInstance.put(`/admin/articles/${editingArticle.id}`, articleData);
                toast.success("Product updated successfully");
            } else {
                // Create new article
                await axiosInstance.post("/admin/articles", articleData);
                toast.success("Product created successfully");
            }
            
            setShowArticleForm(false);
            setEditingArticle(null);
            fetchArticles(pagination.currentPage);
        } catch (error) {
            console.error("Failed to save article:", error);
            toast.error("Failed to save product");
        } finally {
            setIsSubmitting(false);
        }
    };

    // Handle form close
    const handleFormClose = () => {
        setShowArticleForm(false);
        setEditingArticle(null);
    };

    if (!authUser?.isAdmin) {
        return null; // Prevent flash before redirect
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50">
                <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                    <div className="px-4 py-6 sm:px-0">
                        <div className="flex flex-col items-center justify-center h-96">
                            <AlertCircle className="h-16 w-16 text-red-500 mb-4" />
                            <h2 className="text-2xl font-bold text-gray-900 mb-2">Something went wrong</h2>
                            <p className="text-gray-600 mb-4">{error}</p>
                            <button
                                onClick={() => fetchArticles(0)}
                                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                            >
                                Try Again
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                {/* Header */}
                <div className="px-4 py-6 sm:px-0">
                    <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-6">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                                <ShoppingBag className="h-8 w-8 mr-3 text-blue-600" />
                                Manage Products
                            </h1>
                            <p className="text-gray-600 mt-1">
                                Create, edit and manage your product catalog
                            </p>
                        </div>
                        <div className="mt-4 sm:mt-0">
                            <button
                                onClick={handleCreateArticle}
                                className="flex items-center px-4 py-2 bg-blue-600 text-white font-medium rounded-md hover:bg-blue-700 transition-colors"
                            >
                                <Plus className="h-5 w-5 mr-2" />
                                Add Product
                            </button>
                        </div>
                    </div>

                    {/* Search and Filters */}
                    <div className="flex flex-col sm:flex-row gap-4 mb-6">
                        <div className="flex-1">
                            <SearchBar
                                onSearch={handleSearch}
                                placeholder="Search products by name..."
                            />
                        </div>
                        <div>
                            <Filters
                                onFiltersChange={handleFiltersChange}
                                onClear={handleFiltersClear}
                            />
                        </div>
                    </div>

                    {/* Loading State */}
                    {loading && (
                        <div className="flex justify-center items-center h-96">
                            <Loader className="h-8 w-8 animate-spin text-blue-600" />
                            <span className="ml-2 text-gray-600">Loading products...</span>
                        </div>
                    )}

                    {/* No Products */}
                    {!loading && articles.length === 0 && (
                        <div className="flex flex-col items-center justify-center h-96">
                            <Package className="h-16 w-16 text-gray-400 mb-4" />
                            <h2 className="text-2xl font-bold text-gray-900 mb-2">No products found</h2>
                            <p className="text-gray-600 mb-4">
                                {search || filters.minPrice || filters.maxPrice
                                    ? "Try adjusting your search or filters"
                                    : "Start by creating your first product"
                                }
                            </p>
                            <div className="flex space-x-4">
                                {(search || filters.minPrice || filters.maxPrice) && (
                                    <button
                                        onClick={handleFiltersClear}
                                        className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
                                    >
                                        Clear Filters
                                    </button>
                                )}
                                <button
                                    onClick={handleCreateArticle}
                                    className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                                >
                                    <Plus className="h-4 w-4 mr-2" />
                                    Add Product
                                </button>
                            </div>
                        </div>
                    )}

                    {/* Products Grid */}
                    {!loading && articles.length > 0 && (
                        <>
                            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
                                {articles.map((article) => (
                                    <div key={article.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
                                        <div className="aspect-w-1 aspect-h-1 w-full overflow-hidden bg-gray-200">
                                            <img
                                                src={article.imageUrl || "https://via.placeholder.com/300x300/E5E7EB/9CA3AF?text=No+Image"}
                                                alt={article.name}
                                                className="h-48 w-full object-cover object-center"
                                            />
                                        </div>
                                        <div className="p-4">
                                            <h3 className="text-lg font-semibold text-gray-900 mb-1">{article.name}</h3>
                                            <p className="text-gray-600 text-sm mb-2 line-clamp-2">{article.description}</p>
                                            <div className="flex items-center justify-between mb-4">
                                                <span className="text-xl font-bold text-gray-900">{article.price} {article.currency}</span>
                                                <span className="text-sm text-gray-500">
                                                    Stock: {article.availableQuantity}
                                                </span>
                                            </div>
                                            <div className="flex space-x-2">
                                                <button
                                                    onClick={() => handleEditArticle(article)}
                                                    className="flex-1 flex items-center justify-center px-3 py-2 border border-blue-300 text-blue-600 rounded-md hover:bg-blue-50 transition-colors"
                                                >
                                                    <Edit className="h-4 w-4 mr-1" />
                                                    Edit
                                                </button>
                                                <button
                                                    onClick={() => handleDeleteArticle(article.id, article.name)}
                                                    className="flex-1 flex items-center justify-center px-3 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50 transition-colors"
                                                >
                                                    <Trash2 className="h-4 w-4 mr-1" />
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>

                            {/* Pagination */}
                            <Pagination
                                currentPage={pagination.currentPage}
                                totalPages={pagination.totalPages}
                                totalElements={pagination.totalElements}
                                pageSize={pagination.pageSize}
                                onPageChange={handlePageChange}
                            />
                        </>
                    )}
                </div>
            </div>

            {/* Article Form Modal */}
            {showArticleForm && (
                <ArticleForm
                    article={editingArticle}
                    isOpen={showArticleForm}
                    onClose={handleFormClose}
                    onSubmit={handleFormSubmit}
                    isSubmitting={isSubmitting}
                />
            )}
        </div>
    );
};

export default AdminArticlesPage;
